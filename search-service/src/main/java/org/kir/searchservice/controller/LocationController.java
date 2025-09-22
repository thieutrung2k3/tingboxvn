package org.kir.searchservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.response.LocationResponse;
import org.kir.searchservice.service.LocationService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/public/getLocationsByCityName")
    public ApiResponse<List<LocationResponse>> getLocationsByCityName(Pageable pageable,
                                                                      @RequestParam("cityName") String cityName){
        return ApiResponse.data(locationService.getLocationsByCityName(pageable, cityName));
    }
}
