package org.kir.searchservice.service;

import org.kir.searchservice.entity.Trip;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {
    List<Trip> search(Pageable pageable, LocalDateTime departureTime, String originCode, String destinationCode, Long providerId);
}
