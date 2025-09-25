package com.kir.passengerservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import com.kir.passengerservice.dto.response.PassengerResponse;
import com.kir.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/passenger")
public class PassengerController {

      private final PassengerService passengerService;

      @GetMapping("/public/by-account/{accountId}")
      public ApiResponse<java.util.List<PassengerResponse>> getByAccount(@PathVariable("accountId") Long accountId) {
            return ApiResponse.data(passengerService.getByAccountId(accountId));
      }
}
