package com.kir.passengerservice.service;

import com.kir.passengerservice.dto.request.PassengerCreationRequest;
import com.kir.passengerservice.dto.response.PassengerResponse;

public interface PassengerService {
    PassengerResponse createPassenger(PassengerCreationRequest request);

    java.util.List<PassengerResponse> getByAccountId(Long accountId);
}
