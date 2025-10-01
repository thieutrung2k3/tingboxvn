package org.kir.searchservice.dto.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kir.searchservice.entity.Seat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatStatusUpdate {
    private Long seatId;
    private Long tripId;
    private String seatNumber;
    private Seat.SeatStatus status;
    private String previousStatus;
    private Long userId;
    private String userName;
    private LocalDateTime timestamp;
    private String action; // "SELECT", "RELEASE", "RESERVE", "BOOK", "LOCK", "UNLOCK"
    private Long lockExpiryTime; // Unix timestamp when lock expires
    private String message; // Optional message for user
}



