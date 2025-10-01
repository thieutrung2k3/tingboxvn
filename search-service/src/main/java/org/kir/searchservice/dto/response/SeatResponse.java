package org.kir.searchservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kir.searchservice.entity.Seat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponse {
    private Long id;
    private Long tripId;
    private String seatNumber;
    private String seatClass;
    private Integer rowNumber;
    private String columnLetter;
    private Seat.SeatStatus status;
    private Double priceModifier;
    private BigDecimal totalPrice; // base price + modifier
    private Boolean isWindowSeat;
    private Boolean isAisleSeat;
    private Boolean isEmergencyExit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

