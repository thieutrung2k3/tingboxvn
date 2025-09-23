package org.kir.bookingservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.kir.bookingservice.dto.BookingRequest;
import org.kir.bookingservice.dto.BookingResponse;
import org.kir.bookingservice.service.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/public")
    public ApiResponse<BookingResponse> create(@RequestBody BookingRequest request) {
        return ApiResponse.data(bookingService.createBooking(request));
    }
}


