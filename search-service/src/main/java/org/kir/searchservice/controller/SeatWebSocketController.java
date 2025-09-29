package org.kir.searchservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kir.searchservice.dto.websocket.SeatStatusUpdate;
import org.kir.searchservice.service.SeatWebSocketService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeatWebSocketController {

    private final SeatWebSocketService seatWebSocketService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/seat/select/{tripId}")
    public void selectSeat(@DestinationVariable Long tripId, @Payload SeatSelectionMessage message) {
        log.info("Received seat selection request for trip {}: {}", tripId, message);
        
        try {
            SeatStatusUpdate update = seatWebSocketService.selectSeat(tripId, message.getSeatNumber(), message.getUserId(), message.getUserName());
            
            // Broadcast to all clients subscribed to this trip's seat updates
            messagingTemplate.convertAndSend("/topic/seats/" + tripId, update);
            
            // Also send to the specific user who made the selection
            messagingTemplate.convertAndSendToUser(
                message.getUserId().toString(), 
                "/queue/seat-updates", 
                update
            );
            
        } catch (Exception e) {
            log.error("Error selecting seat: {}", e.getMessage());
            // Send error message back to user
            SeatStatusUpdate errorUpdate = SeatStatusUpdate.builder()
                .seatId(null)
                .tripId(tripId)
                .seatNumber(message.getSeatNumber())
                .status(null)
                .action("ERROR")
                .message("Failed to select seat: " + e.getMessage())
                .timestamp(java.time.LocalDateTime.now())
                .build();
                
            messagingTemplate.convertAndSendToUser(
                message.getUserId().toString(), 
                "/queue/seat-errors", 
                errorUpdate
            );
        }
    }

    @MessageMapping("/seat/release/{tripId}")
    public void releaseSeat(@DestinationVariable Long tripId, @Payload SeatSelectionMessage message) {
        log.info("Received seat release request for trip {}: {}", tripId, message);
        
        try {
            SeatStatusUpdate update = seatWebSocketService.releaseSeat(tripId, message.getSeatNumber(), message.getUserId());
            
            // Broadcast to all clients
            messagingTemplate.convertAndSend("/topic/seats/" + tripId, update);
            
        } catch (Exception e) {
            log.error("Error releasing seat: {}", e.getMessage());
        }
    }

    @MessageMapping("/seat/reserve/{tripId}")
    public void reserveSeat(@DestinationVariable Long tripId, @Payload SeatSelectionMessage message) {
        log.info("Received seat reservation request for trip {}: {}", tripId, message);
        
        try {
            SeatStatusUpdate update = seatWebSocketService.reserveSeat(tripId, message.getSeatNumber(), message.getUserId(), message.getUserName());
            
            // Broadcast to all clients
            messagingTemplate.convertAndSend("/topic/seats/" + tripId, update);
            
        } catch (Exception e) {
            log.error("Error reserving seat: {}", e.getMessage());
        }
    }

    @MessageMapping("/seat/heartbeat/{tripId}")
    public void heartbeat(@DestinationVariable Long tripId, @Payload HeartbeatMessage message) {
        log.debug("Received heartbeat from user {} for trip {}", message.getUserId(), tripId);
        
        // Update user's last activity for seat locks
        seatWebSocketService.updateUserActivity(tripId, message.getUserId());
    }

    // Inner classes for WebSocket message payloads
    public static class SeatSelectionMessage {
        private String seatNumber;
        private Long userId;
        private String userName;
        private String sessionId;

        // Getters and setters
        public String getSeatNumber() { return seatNumber; }
        public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    }

    public static class HeartbeatMessage {
        private Long userId;
        private String sessionId;
        private Long timestamp;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public Long getTimestamp() { return timestamp; }
        public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    }
}
