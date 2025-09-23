package org.kir.bookingservice.service;

import org.kir.bookingservice.dto.BookingRequest;
import org.kir.bookingservice.dto.BookingResponse;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
}


