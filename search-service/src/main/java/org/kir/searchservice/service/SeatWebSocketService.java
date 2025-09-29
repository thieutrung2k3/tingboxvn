package org.kir.searchservice.service;

import org.kir.searchservice.dto.websocket.SeatLockInfo;
import org.kir.searchservice.dto.websocket.SeatStatusUpdate;
import org.kir.searchservice.entity.Seat;

import java.util.List;
import java.util.Map;

public interface SeatWebSocketService {
    
    SeatStatusUpdate selectSeat(Long tripId, String seatNumber, Long userId, String userName);
    
    SeatStatusUpdate releaseSeat(Long tripId, String seatNumber, Long userId);
    
    SeatStatusUpdate reserveSeat(Long tripId, String seatNumber, Long userId, String userName);
    
    SeatStatusUpdate bookSeat(Long tripId, String seatNumber, Long userId);
    
    void updateUserActivity(Long tripId, Long userId);
    
    List<SeatLockInfo> getActiveLocks(Long tripId);
    
    Map<String, SeatLockInfo> getUserLocks(Long tripId, Long userId);
    
    void cleanupExpiredLocks();
    
    boolean isSeatLocked(Long tripId, String seatNumber);
    
    boolean isSeatLockedByUser(Long tripId, String seatNumber, Long userId);
}
