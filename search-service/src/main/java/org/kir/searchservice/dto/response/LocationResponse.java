package org.kir.searchservice.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationResponse {
    private Long id;

    private String iata;

    private String icao;

    private String name;

    private String location;

    private String time;

    private String cityName;
}
