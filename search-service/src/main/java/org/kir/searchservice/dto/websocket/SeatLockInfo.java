package org.kir.searchservice.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatLockInfo {
    private Long seatId;
    private Long tripId;
    private String seatNumber;
    private Long userId;
    private String userName;
    private LocalDateTime lockedAt;
    private LocalDateTime expiresAt;
    private Long lockDurationSeconds;
    private String lockId; // Unique identifier for this lock
}



