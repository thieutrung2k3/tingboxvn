package org.kir.searchservice.mapper;

import org.kir.searchservice.dto.response.LocationResponse;
import org.kir.searchservice.entity.Location;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocationMapper {
    public LocationResponse toLocationResponse(Location location) {
        LocationResponse response = LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .time(location.getTime())
                .location(location.getLocation())
                .iata(location.getIata() != null
                        ? new ArrayList<>(List.of(location.getIata()))
                        : new ArrayList<>())
                .build();

        String[] parts = location.getLocation().split(",");
        String cityName = parts[parts.length - 1].trim();
        response.setCityName(cityName);

        return response;
    }
}
