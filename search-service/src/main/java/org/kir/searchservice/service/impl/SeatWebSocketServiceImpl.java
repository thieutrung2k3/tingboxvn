package org.kir.searchservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kir.searchservice.dto.websocket.SeatLockInfo;
import org.kir.searchservice.dto.websocket.SeatStatusUpdate;
import org.kir.searchservice.entity.Seat;
import org.kir.searchservice.repository.SeatRepository;
import org.kir.searchservice.service.SeatWebSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatWebSocketServiceImpl implements SeatWebSocketService {

    private final SeatRepository seatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    // In-memory storage for seat locks (in production, use Redis)
    private final Map<String, SeatLockInfo> seatLocks = new ConcurrentHashMap<>();
    private final Map<Long, Set<String>> userLocks = new ConcurrentHashMap<>();
    private final Map<Long, LocalDateTime> userLastActivity = new ConcurrentHashMap<>();
    
    // Lock duration in minutes
    private static final int LOCK_DURATION_MINUTES = 5;
    
    @Override
    @Transactional
    public SeatStatusUpdate selectSeat(Long tripId, String seatNumber, Long userId, String userName) {
        Seat seat = seatRepository.findByTripIdAndSeatNumberAndIsDeleteFalse(tripId, seatNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        String lockKey = generateLockKey(tripId, seatNumber);
        SeatLockInfo existingLock = seatLocks.get(lockKey);
        
        // Check if seat is already locked by someone else
        if (existingLock != null && !existingLock.getUserId().equals(userId)) {
            if (isLockExpired(existingLock)) {
                // Remove expired lock
                removeLock(lockKey, existingLock);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat is currently locked by another user");
            }
        }

        // Check if seat is available
        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not available");
        }

        // Create new lock
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(LOCK_DURATION_MINUTES);
        
        SeatLockInfo lockInfo = SeatLockInfo.builder()
                .seatId(seat.getId())
                .tripId(tripId)
                .seatNumber(seatNumber)
                .userId(userId)
                .userName(userName)
                .lockedAt(now)
                .expiresAt(expiresAt)
                .lockDurationSeconds(LOCK_DURATION_MINUTES * 60L)
                .lockId(UUID.randomUUID().toString())
                .build();

        // Store lock
        seatLocks.put(lockKey, lockInfo);
        userLocks.computeIfAbsent(userId, k -> new HashSet<>()).add(lockKey);
        userLastActivity.put(userId, now);

        // Update seat status to LOCKED (if you have this status) or keep AVAILABLE but track lock
        // For now, we'll keep the seat as AVAILABLE but track the lock separately
        
        return SeatStatusUpdate.builder()
                .seatId(seat.getId())
                .tripId(tripId)
                .seatNumber(seatNumber)
                .status(Seat.SeatStatus.AVAILABLE) // Keep as available, but locked
                .previousStatus(String.valueOf(Seat.SeatStatus.AVAILABLE))
                .userId(userId)
                .userName(userName)
                .timestamp(now)
                .action("LOCK")
                .lockExpiryTime(expiresAt.atZone(java.time.ZoneId.systemDefault()).toEpochSecond())
                .message("Seat locked for " + LOCK_DURATION_MINUTES + " minutes")
                .build();
    }

    @Override
    @Transactional
    public SeatStatusUpdate releaseSeat(Long tripId, String seatNumber, Long userId) {
        String lockKey = generateLockKey(tripId, seatNumber);
        SeatLockInfo lockInfo = seatLocks.get(lockKey);
        
        if (lockInfo == null || !lockInfo.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not locked by this user");
        }

        // Remove lock
        removeLock(lockKey, lockInfo);

        Seat seat = seatRepository.findByTripIdAndSeatNumberAndIsDeleteFalse(tripId, seatNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        return SeatStatusUpdate.builder()
                .seatId(seat.getId())
                .tripId(tripId)
                .seatNumber(seatNumber)
                .status(Seat.SeatStatus.AVAILABLE)
                .previousStatus(String.valueOf(Seat.SeatStatus.AVAILABLE))
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .action("UNLOCK")
                .message("Seat lock released")
                .build();
    }

    @Override
    @Transactional
    public SeatStatusUpdate reserveSeat(Long tripId, String seatNumber, Long userId, String userName) {
        // First select/lock the seat
        SeatStatusUpdate lockUpdate = selectSeat(tripId, seatNumber, userId, userName);
        
        // Then update seat status to RESERVED
        Seat seat = seatRepository.findByTripIdAndSeatNumberAndIsDeleteFalse(tripId, seatNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        Seat.SeatStatus previousStatus = seat.getStatus();
        seat.setStatus(Seat.SeatStatus.RESERVED);
        seatRepository.save(seat);

        return SeatStatusUpdate.builder()
                .seatId(seat.getId())
                .tripId(tripId)
                .seatNumber(seatNumber)
                .status(Seat.SeatStatus.RESERVED)
                .previousStatus(String.valueOf(previousStatus))
                .userId(userId)
                .userName(userName)
                .timestamp(LocalDateTime.now())
                .action("RESERVE")
                .message("Seat reserved")
                .build();
    }

    @Override
    @Transactional
    public SeatStatusUpdate bookSeat(Long tripId, String seatNumber, Long userId) {
        String lockKey = generateLockKey(tripId, seatNumber);
        SeatLockInfo lockInfo = seatLocks.get(lockKey);
        
        if (lockInfo == null || !lockInfo.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat is not locked by this user");
        }

        Seat seat = seatRepository.findByTripIdAndSeatNumberAndIsDeleteFalse(tripId, seatNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seat not found"));

        Seat.SeatStatus previousStatus = seat.getStatus();
        seat.setStatus(Seat.SeatStatus.OCCUPIED);
        seatRepository.save(seat);

        // Remove lock after booking
        removeLock(lockKey, lockInfo);

        return SeatStatusUpdate.builder()
                .seatId(seat.getId())
                .tripId(tripId)
                .seatNumber(seatNumber)
                .status(Seat.SeatStatus.OCCUPIED)
                .previousStatus(String.valueOf(previousStatus))
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .action("BOOK")
                .message("Seat booked successfully")
                .build();
    }

    @Override
    public void updateUserActivity(Long tripId, Long userId) {
        userLastActivity.put(userId, LocalDateTime.now());
    }

    @Override
    public List<SeatLockInfo> getActiveLocks(Long tripId) {
        return seatLocks.values().stream()
                .filter(lock -> lock.getTripId().equals(tripId))
                .filter(lock -> !isLockExpired(lock))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, SeatLockInfo> getUserLocks(Long tripId, Long userId) {
        Set<String> userLockKeys = userLocks.getOrDefault(userId, Collections.emptySet());
        return userLockKeys.stream()
                .map(seatLocks::get)
                .filter(Objects::nonNull)
                .filter(lock -> lock.getTripId().equals(tripId))
                .filter(lock -> !isLockExpired(lock))
                .collect(Collectors.toMap(SeatLockInfo::getSeatNumber, lock -> lock));
    }

    @Override
    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredLocks() {
        LocalDateTime now = LocalDateTime.now();
        List<String> expiredLocks = new ArrayList<>();
        
        for (Map.Entry<String, SeatLockInfo> entry : seatLocks.entrySet()) {
            if (isLockExpired(entry.getValue())) {
                expiredLocks.add(entry.getKey());
            }
        }
        
        for (String lockKey : expiredLocks) {
            SeatLockInfo lockInfo = seatLocks.get(lockKey);
            if (lockInfo != null) {
                removeLock(lockKey, lockInfo);
                
                // Broadcast unlock message
                SeatStatusUpdate update = SeatStatusUpdate.builder()
                        .seatId(lockInfo.getSeatId())
                        .tripId(lockInfo.getTripId())
                        .seatNumber(lockInfo.getSeatNumber())
                        .status(Seat.SeatStatus.AVAILABLE)
                        .previousStatus(String.valueOf(Seat.SeatStatus.AVAILABLE))
                        .userId(lockInfo.getUserId())
                        .userName(lockInfo.getUserName())
                        .timestamp(now)
                        .action("AUTO_UNLOCK")
                        .message("Seat lock expired and was automatically released")
                        .build();
                
                messagingTemplate.convertAndSend("/topic/seats/" + lockInfo.getTripId(), update);
            }
        }
        
        if (!expiredLocks.isEmpty()) {
            log.info("Cleaned up {} expired seat locks", expiredLocks.size());
        }
    }

    @Override
    public boolean isSeatLocked(Long tripId, String seatNumber) {
        String lockKey = generateLockKey(tripId, seatNumber);
        SeatLockInfo lockInfo = seatLocks.get(lockKey);
        return lockInfo != null && !isLockExpired(lockInfo);
    }

    @Override
    public boolean isSeatLockedByUser(Long tripId, String seatNumber, Long userId) {
        String lockKey = generateLockKey(tripId, seatNumber);
        SeatLockInfo lockInfo = seatLocks.get(lockKey);
        return lockInfo != null && lockInfo.getUserId().equals(userId) && !isLockExpired(lockInfo);
    }

    private String generateLockKey(Long tripId, String seatNumber) {
        return tripId + ":" + seatNumber;
    }

    private boolean isLockExpired(SeatLockInfo lockInfo) {
        return lockInfo.getExpiresAt().isBefore(LocalDateTime.now());
    }

    private void removeLock(String lockKey, SeatLockInfo lockInfo) {
        seatLocks.remove(lockKey);
        Set<String> userLockSet = userLocks.get(lockInfo.getUserId());
        if (userLockSet != null) {
            userLockSet.remove(lockKey);
            if (userLockSet.isEmpty()) {
                userLocks.remove(lockInfo.getUserId());
            }
        }
    }
}
