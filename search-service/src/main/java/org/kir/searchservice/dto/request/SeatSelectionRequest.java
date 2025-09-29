package org.kir.searchservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatSelectionRequest {
    private Long tripId;
    private List<String> seatNumbers;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
}
