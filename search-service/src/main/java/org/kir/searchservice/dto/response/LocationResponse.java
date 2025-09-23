package org.kir.searchservice.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class LocationResponse {
    private Long id;

    @Builder.Default
    private List<String> iata = new ArrayList<>();

    private String name;

    private String location;

    private String time;

    private String cityName;
}
