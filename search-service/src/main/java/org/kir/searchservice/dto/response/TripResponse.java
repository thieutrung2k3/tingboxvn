package org.kir.searchservice.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.kir.searchservice.entity.Provider;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TripResponse {
    private Long id;

    private String tripReferenceId;

    private String providerId;

    private String originCode;

    private String destinationCode;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private String vehicleInfo;

    private String tripType;

    private BigDecimal basePrice;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Provider provider;
}
