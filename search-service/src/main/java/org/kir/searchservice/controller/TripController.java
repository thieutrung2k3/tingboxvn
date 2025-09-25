package org.kir.searchservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.response.TripResponse;
import org.kir.searchservice.service.TripService;
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
    public ApiResponse<List<TripResponse>> search(Pageable pageable,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(value = "originCodes", required = false) List<String> originCodes,
            @RequestParam(value = "destinationCodes", required = false) List<String> destinationCodes,
            @RequestParam(value = "providerId", required = false) Long providerId) {
        return ApiResponse.data(tripService.search(pageable, from, to, originCodes, destinationCodes, providerId));
    }
}
