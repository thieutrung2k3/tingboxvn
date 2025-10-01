package org.kir.searchservice.service;

import org.kir.searchservice.dto.request.ProviderUpdateRequest;
import org.kir.searchservice.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProviderService {
    List<Provider> getProviders(Pageable pageable, String keyword, String paymentScheduled);

    void updateProvider(Long providerId, ProviderUpdateRequest request);
}
