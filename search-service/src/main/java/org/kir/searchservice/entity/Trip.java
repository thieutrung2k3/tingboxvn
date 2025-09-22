package org.kir.searchservice.entity;

import com.kir.commonservice.constant.AppConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_reference_id", unique = true, nullable = false)
    private String tripReferenceId;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "origin_code", nullable = false)
    private String originCode;

    @Column(name = "destination_code", nullable = false)
    private String destinationCode;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "vehicle_info", nullable = false)
    private String vehicleInfo;

    @Column(name = "trip_type", nullable = false)
    private String tripType;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "status", nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
