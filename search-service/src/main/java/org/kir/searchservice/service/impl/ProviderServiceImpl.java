package org.kir.searchservice.service.impl;

import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.request.ProviderUpdateRequest;
import org.kir.searchservice.entity.Provider;
import org.kir.searchservice.mapper.ProviderMapper;
import org.kir.searchservice.repository.ProviderRepository;
import org.kir.searchservice.service.ProviderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    @Override
    public List<Provider> getProviders(Pageable pageable, String keyword, String paymentScheduled) {
        Page<Provider> providers = providerRepository.findProviders(pageable, keyword, paymentScheduled);
        return providers.getContent();
    }

    @Override
    public void updateProvider(Long providerId, ProviderUpdateRequest request) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new AppException(ErrorCode.PROVIDER_NOT_FOUND));

        providerMapper.updateProviderFromRequest(request, provider);
        providerRepository.save(provider);
    }
}
