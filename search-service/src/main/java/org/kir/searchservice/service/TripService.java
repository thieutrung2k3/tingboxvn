package org.kir.searchservice.service;

import org.kir.searchservice.dto.response.TripResponse;
import org.kir.searchservice.entity.Trip;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {
    List<TripResponse> search(Pageable pageable, LocalDateTime departureTime, List<String> originCodes,
            List<String> destinationCodes, Long providerId);
}
