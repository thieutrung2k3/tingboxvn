package com.kir.passengerservice.controller.internal;

import com.kir.commonservice.dto.ApiResponse;
import com.kir.passengerservice.dto.request.PassengerCreationRequest;
import com.kir.passengerservice.dto.response.PassengerResponse;
import com.kir.passengerservice.entity.Passenger;
import com.kir.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
//@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerInternalController {
    private final PassengerService passengerService;

    @PostMapping("/internal/create")
    public ApiResponse<PassengerResponse> createPassenger(@RequestBody PassengerCreationRequest request) {
        log.info("createPassenger request={}", request);
        return ApiResponse.data(passengerService.createPassenger(request));
    }
}
