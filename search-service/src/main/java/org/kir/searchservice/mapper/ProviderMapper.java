package org.kir.searchservice.mapper;

import org.kir.searchservice.dto.request.ProviderUpdateRequest;
import org.kir.searchservice.entity.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {
    public void updateProviderFromRequest(ProviderUpdateRequest request, Provider provider) {
        if (request.getName() != null) {
            provider.setName(request.getName());
        }
        if (request.getApiEndpoint() != null) {
            provider.setApiEndpoint(request.getApiEndpoint());
        }
        if (request.getCommissionRate() != null) {
            provider.setCommissionRate(request.getCommissionRate());
        }
        if (request.getPaymentSchedule() != null) {
            provider.setPaymentSchedule(request.getPaymentSchedule());
        }
        if (request.getCurrency() != null) {
            provider.setCurrency(request.getCurrency());
        }
        if (request.getImageUrl() != null) {
            provider.setImageUrl(request.getImageUrl());
        }
    }
}
