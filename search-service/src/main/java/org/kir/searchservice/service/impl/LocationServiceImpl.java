package org.kir.searchservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.response.LocationResponse;
import org.kir.searchservice.entity.Location;
import org.kir.searchservice.mapper.LocationMapper;
import org.kir.searchservice.repository.LocationRepository;
import org.kir.searchservice.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public List<LocationResponse> getLocationsByCityName(Pageable pageable, String cityName) {
        Page<Location> locationPage = locationRepository.findByLocationKey(cityName, pageable);
        List<Location> locations = locationPage.getContent();
        List<LocationResponse> locationResponses = locations.stream().map(locationMapper::toLocationResponse).toList();
        return locationResponses;
    }
}
