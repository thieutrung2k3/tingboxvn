package com.kir.passengerservice.mapper;

import com.kir.passengerservice.dto.request.PassengerCreationRequest;
import com.kir.passengerservice.dto.response.PassengerResponse;
import com.kir.passengerservice.entity.Passenger;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    @Mapping(target = "isDelete", constant = "false")
    Passenger toPassenger(PassengerCreationRequest request);

    PassengerResponse toPassengerResponse(Passenger passenger);

    @AfterMapping
    default void setDefaultValues(@MappingTarget Passenger passenger){
        passenger.setIsDelete(false);
    }
}
