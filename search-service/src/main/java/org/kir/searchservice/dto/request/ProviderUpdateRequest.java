package org.kir.searchservice.dto.request;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProviderUpdateRequest {
    private String name;
    private String apiEndpoint;
    private BigDecimal commissionRate;
    private String paymentSchedule; //DAILY, WEEKLY, ...
    private String currency; //Đơn vị tiền tệ
    private String imageUrl;
}
