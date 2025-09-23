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
public class BookingRequest {
    private Long customerId;
    private String customerEmail;

    private String itemType; // flight
    private String itemReferenceId; // trip reference id
    private String departureLocationCode;
    private String arrivalLocationCode;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;

    private List<PassengerRequest> passengers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerRequest {
        private String firstName;
        private String lastName;
        private String gender;
        private String identityDocumentType;
        private String identityDocumentNumber;
        private String seatNumber;
    }
}


