package org.kir.searchservice.service;

import org.kir.searchservice.dto.response.LocationResponse;
import org.kir.searchservice.dto.response.LocationSearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {
    LocationSearchResponse getLocationsByCityName(Pageable pageable, String cityName);
}
