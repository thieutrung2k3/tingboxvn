package org.kir.searchservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.response.LocationResponse;
import org.kir.searchservice.dto.response.LocationSearchResponse;
import org.kir.searchservice.entity.Location;
import org.kir.searchservice.mapper.LocationMapper;
import org.kir.searchservice.repository.LocationRepository;
import org.kir.searchservice.service.LocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationSearchResponse getLocationsByCityName(Pageable pageable, String cityName) {
        // Lấy danh sách theo cityName
        Page<Location> locationPage = locationRepository.findByLocationKey(cityName, pageable);
        List<LocationResponse> uniqueByCity = groupByCityName(
                locationPage.getContent().stream()
                        .map(locationMapper::toLocationResponse)
                        .toList()
        );

        // Danh sách sân bay đại diện quanh VN
        List<String> iataList = getFamousIataCodes();

        // Lấy famous cities
        List<LocationResponse> famousCities = groupByCityName(
                locationRepository.findFamousCity(iataList).stream()
                        .map(locationMapper::toLocationResponse)
                        .toList()
        );

        return LocationSearchResponse.builder()
                .famousLocations(famousCities)
                .locations(uniqueByCity)
                .build();
    }

    /**
     * Gom nhóm theo cityName, merge các iata lại thành list unique
     */
    private List<LocationResponse> groupByCityName(List<LocationResponse> locations) {
        return locations.stream()
                .collect(Collectors.groupingBy(
                        LocationResponse::getCityName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    LocationResponse first = list.get(0);
                                    return LocationResponse.builder()
                                            .id(first.getId())
                                            .iata(list.stream()
                                                    .flatMap(loc -> loc.getIata().stream()) // đã null-safe
                                                    .distinct()
                                                    .toList())
                                            .name(first.getName())
                                            .location(first.getLocation())
                                            .time(first.getTime())
                                            .cityName(first.getCityName())
                                            .build();
                                }
                        )
                ))
                .values()
                .stream()
                .toList();
    }

    /**
     * Trả về danh sách IATA code đại diện cho các nước quanh VN
     */
    private List<String> getFamousIataCodes() {
        return List.of(
                "HAN", // Hà Nội - Nội Bài
                "PNH", // Phnom Penh - Campuchia
                "VTE", // Vientiane - Lào
                "BKK", // Bangkok - Thái Lan
                "KUL", // Kuala Lumpur - Malaysia
                "SIN", // Singapore Changi
                "MNL", // Manila - Philippines
                "RGN", // Yangon - Myanmar
                "ICN", // Seoul - Incheon, Hàn Quốc
                "NRT", // Tokyo - Narita, Nhật Bản
                "TPE", // Taipei - Taoyuan, Đài Loan
                "PEK"  // Bắc Kinh - China
        );
    }
}
