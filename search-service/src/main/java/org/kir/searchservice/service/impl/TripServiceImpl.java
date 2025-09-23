package org.kir.searchservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.response.TripResponse;
import org.kir.searchservice.entity.Provider;
import org.kir.searchservice.entity.Trip;
import org.kir.searchservice.mapper.TripMapper;
import org.kir.searchservice.repository.ProviderRepository;
import org.kir.searchservice.repository.TripRepository;
import org.kir.searchservice.service.TripService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final ProviderRepository providerRepository;

    @Override
    public List<TripResponse> search(Pageable pageable, LocalDateTime departureTime, List<String> originCodes,
            List<String> destinationCodes, Long providerId) {
        Page<Trip> tripPage = tripRepository.searchWithPageable(departureTime, originCodes, destinationCodes,
                providerId, pageable);
        List<Trip> trips = tripPage.getContent();

        // To response
        Map<Long, Provider> mapProviders = new HashMap<>();
        List<Provider> providers = providerRepository.findAll();
        providers.forEach(provider -> mapProviders.put(provider.getId(), provider));

        TripMapper tripMapper = new TripMapper(providers);

        List<TripResponse> tripResponses = tripMapper.toResponseList(trips);

        return tripResponses;
    }
}
