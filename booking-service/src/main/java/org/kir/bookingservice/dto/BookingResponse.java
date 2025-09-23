package org.kir.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long orderId;
    private String orderCode;
    private String bookingStatus;
    private String paymentStatus;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private List<TicketInfo> tickets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketInfo {
        private Long orderItemId;
        private String seatNumber;
        private String qrImageBase64;
    }
}


