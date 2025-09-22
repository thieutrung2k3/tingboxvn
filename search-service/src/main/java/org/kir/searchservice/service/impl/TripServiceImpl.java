package org.kir.searchservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.kir.searchservice.entity.Trip;
import org.kir.searchservice.repository.TripRepository;
import org.kir.searchservice.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    @Override
    public List<Trip> search(Pageable pageable, LocalDateTime departureTime, String originCode, String destinationCode, Long providerId) {
        Page<Trip> tripPage = tripRepository.searchWithPageable(departureTime, originCode, destinationCode, providerId, pageable);
        List<Trip> trips = tripPage.getContent();

        return trips;
    }
}
