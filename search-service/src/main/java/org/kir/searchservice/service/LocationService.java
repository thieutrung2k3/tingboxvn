package org.kir.searchservice.service;

import org.kir.searchservice.dto.response.LocationResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {
    List<LocationResponse> getLocationsByCityName(Pageable pageable, String cityName);
}
