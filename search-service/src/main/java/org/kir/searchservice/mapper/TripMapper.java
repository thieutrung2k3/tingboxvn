package org.kir.searchservice.mapper;

import org.kir.searchservice.dto.response.TripResponse;
import org.kir.searchservice.entity.Provider;
import org.kir.searchservice.entity.Trip;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TripMapper {

    private final Map<Long, Provider> mapProviders;

    public TripMapper(List<Provider> providers) {
        this.mapProviders = providers.stream()
                .collect(Collectors.toMap(Provider::getId, Function.identity()));
    }

    public TripResponse toResponse(Trip trip) {
        if (trip == null) return null;

        return TripResponse.builder()
                .id(trip.getId())
                .tripReferenceId(trip.getTripReferenceId())
                .providerId(trip.getProviderId() != null ? trip.getProviderId().toString() : null)
                .originCode(trip.getOriginCode())
                .destinationCode(trip.getDestinationCode())
                .departureTime(trip.getDepartureTime())
                .arrivalTime(trip.getArrivalTime())
                .vehicleInfo(trip.getVehicleInfo())
                .tripType(trip.getTripType())
                .basePrice(trip.getBasePrice())
                .status(trip.getStatus())
                .createdAt(trip.getCreatedAt())
                .updatedAt(trip.getUpdatedAt())
                .provider(trip.getProviderId() != null ? mapProviders.get(trip.getProviderId()) : null)
                .build();
    }

    public List<TripResponse> toResponseList(List<Trip> trips) {
        if (trips == null) return Collections.emptyList();
        return trips.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}

