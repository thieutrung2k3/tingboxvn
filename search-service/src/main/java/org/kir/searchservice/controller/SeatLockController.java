package org.kir.searchservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.websocket.SeatLockInfo;
import org.kir.searchservice.service.SeatWebSocketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seat/lock")
public class SeatLockController {

    private final SeatWebSocketService seatWebSocketService;

    @GetMapping("/active/{tripId}")
    public ApiResponse<List<SeatLockInfo>> getActiveLocks(@PathVariable Long tripId) {
        return ApiResponse.data(seatWebSocketService.getActiveLocks(tripId));
    }

    @GetMapping("/user/{tripId}/{userId}")
    public ApiResponse<Map<String, SeatLockInfo>> getUserLocks(
            @PathVariable Long tripId, 
            @PathVariable Long userId) {
        return ApiResponse.data(seatWebSocketService.getUserLocks(tripId, userId));
    }

    @GetMapping("/check/{tripId}/{seatNumber}")
    public ApiResponse<Boolean> isSeatLocked(
            @PathVariable Long tripId, 
            @PathVariable String seatNumber) {
        return ApiResponse.data(seatWebSocketService.isSeatLocked(tripId, seatNumber));
    }

    @GetMapping("/check-user/{tripId}/{seatNumber}/{userId}")
    public ApiResponse<Boolean> isSeatLockedByUser(
            @PathVariable Long tripId, 
            @PathVariable String seatNumber,
            @PathVariable Long userId) {
        return ApiResponse.data(seatWebSocketService.isSeatLockedByUser(tripId, seatNumber, userId));
    }

    @PostMapping("/cleanup")
    public ApiResponse<String> cleanupExpiredLocks() {
        seatWebSocketService.cleanupExpiredLocks();
        return ApiResponse.data("Cleanup completed");
    }
}



