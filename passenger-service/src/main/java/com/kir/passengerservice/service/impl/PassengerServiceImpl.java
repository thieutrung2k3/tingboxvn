package com.kir.passengerservice.service.impl;

import com.kir.passengerservice.dto.request.PassengerCreationRequest;
import com.kir.passengerservice.dto.response.PassengerResponse;
import com.kir.passengerservice.entity.Passenger;
import com.kir.passengerservice.mapper.PassengerMapper;
import com.kir.passengerservice.repository.PassengerRepository;
import com.kir.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;

    private final PassengerMapper passengerMapper;

    @Override
    public PassengerResponse createPassenger(PassengerCreationRequest request) {
        Passenger passenger = passengerMapper.toPassenger(request);
        passengerRepository.save(passenger);
        return passengerMapper.toPassengerResponse(passenger);
    }

    @Override
    public List<PassengerResponse> getByAccountId(Long accountId) {
        List<Passenger> passengers = passengerRepository.findAllByAccountIdAndIsDeleteFalse(accountId);
        return passengers.stream()
                .map(passengerMapper::toPassengerResponse)
                .collect(Collectors.toList());
    }
}
