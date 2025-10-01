package org.kir.searchservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.request.SeatSelectionRequest;
import org.kir.searchservice.dto.response.SeatMapResponse;
import org.kir.searchservice.dto.response.SeatResponse;
import org.kir.searchservice.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seat")
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/public/map/{tripId}")
    public ApiResponse<SeatMapResponse> getSeatMap(@PathVariable Long tripId) {
        return ApiResponse.data(seatService.getSeatMapByTripId(tripId));
    }

    @GetMapping("/public/available/{tripId}")
    public ApiResponse<List<SeatResponse>> getAvailableSeats(@PathVariable Long tripId) {
        return ApiResponse.data(seatService.getAvailableSeatsByTripId(tripId));
    }

    @GetMapping("/public/available/{tripId}/{seatClass}")
    public ApiResponse<List<SeatResponse>> getAvailableSeatsByClass(
            @PathVariable Long tripId, 
            @PathVariable String seatClass) {
        return ApiResponse.data(seatService.getAvailableSeatsByTripIdAndClass(tripId, seatClass));
    }

    @PostMapping("/public/select/{tripId}/{seatNumber}")
    public ApiResponse<SeatResponse> selectSeat(
            @PathVariable Long tripId, 
            @PathVariable String seatNumber) {
        return ApiResponse.data(seatService.selectSeat(tripId, seatNumber));
    }

    @PostMapping("/public/select-multiple")
    public ApiResponse<List<SeatResponse>> selectMultipleSeats(@RequestBody SeatSelectionRequest request) {
        return ApiResponse.data(seatService.selectMultipleSeats(request));
    }

    @PostMapping("/public/release/{tripId}/{seatNumber}")
    public ApiResponse<SeatResponse> releaseSeat(
            @PathVariable Long tripId, 
            @PathVariable String seatNumber) {
        return ApiResponse.data(seatService.releaseSeat(tripId, seatNumber));
    }

    @PostMapping("/public/release-multiple")
    public ApiResponse<List<SeatResponse>> releaseMultipleSeats(
            @PathVariable Long tripId, 
            @RequestBody List<String> seatNumbers) {
        return ApiResponse.data(seatService.releaseMultipleSeats(tripId, seatNumbers));
    }

    @PostMapping("/public/reserve/{tripId}/{seatNumber}")
    public ApiResponse<SeatResponse> reserveSeat(
            @PathVariable Long tripId, 
            @PathVariable String seatNumber) {
        return ApiResponse.data(seatService.reserveSeat(tripId, seatNumber));
    }

    @PostMapping("/public/reserve-multiple")
    public ApiResponse<List<SeatResponse>> reserveMultipleSeats(@RequestBody SeatSelectionRequest request) {
        return ApiResponse.data(seatService.reserveMultipleSeats(request));
    }
}



