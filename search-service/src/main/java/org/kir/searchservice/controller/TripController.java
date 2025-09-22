package org.kir.searchservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.kir.searchservice.entity.Trip;
import org.kir.searchservice.service.TripService;
import org.kir.searchservice.service.impl.TripServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;

    @GetMapping("/public/search")
    public ApiResponse<List<Trip>> search(Pageable pageable,
                                          @RequestParam(value = "departureTime", required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS") LocalDateTime departureTime,
                                          @RequestParam(value = "originCode", required = false) String originCode,
                                          @RequestParam(value = "destinationCode", required = false) String destinationCode,
                                          @RequestParam(value = "providerId", required = false) Long providerId) {
        return ApiResponse.data(tripService.search(pageable, departureTime, originCode, destinationCode, providerId));
    }
}
