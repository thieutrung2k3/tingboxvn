package org.kir.searchservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LocationSearchResponse {
    List<LocationResponse> locations;
    List<LocationResponse> famousLocations;
}
