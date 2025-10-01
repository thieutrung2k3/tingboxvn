package org.kir.searchservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatMapResponse {
    private Long tripId;
    private String tripReferenceId;
    private String vehicleInfo;
    private Map<String, List<SeatResponse>> seatsByClass; // "ECONOMY" -> List<SeatResponse>
    private Map<String, Long> availableCountByClass; // "ECONOMY" -> count
    private Integer totalRows;
    private Integer totalColumns;
    private List<String> columnLetters; // ["A", "B", "C", "D"]
}

