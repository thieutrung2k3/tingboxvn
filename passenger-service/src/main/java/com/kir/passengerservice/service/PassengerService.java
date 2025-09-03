package com.kir.passengerservice.service;

import com.kir.passengerservice.dto.request.PassengerCreationRequest;
import com.kir.passengerservice.dto.response.PassengerResponse;
import com.kir.passengerservice.entity.Passenger;

public interface PassengerService {
    PassengerResponse createPassenger(PassengerCreationRequest request);
}
