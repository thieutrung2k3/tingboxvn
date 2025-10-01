package org.kir.searchservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.kir.searchservice.dto.request.ProviderUpdateRequest;
import org.kir.searchservice.entity.Provider;
import org.kir.searchservice.service.ProviderService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping("/admin/getProviders")
    public ApiResponse<List<Provider>> getProviders(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "paymentScheduled", required = false) String paymentScheduled
    ) {
        return ApiResponse.data(providerService.getProviders(pageable, keyword, paymentScheduled));
    }

    @PutMapping("/admin/update")
    public ApiResponse<Void> updateProvider(@RequestParam("providerId") Long providerId,
                                            @RequestBody ProviderUpdateRequest request) {
        providerService.updateProvider(providerId, request);
        return ApiResponse.success(null);
    }
}
