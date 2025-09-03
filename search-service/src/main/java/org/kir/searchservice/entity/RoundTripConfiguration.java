package org.kir.searchservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "round_trip_configurations")
public class RoundTripConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;


    @Column(name = "min_days_stay")
    private Integer minDaysStay;

    @Column(name = "max_days_stay")
    private Integer maxDaysStay;

    @Column(name = "origin_codes", length = 255)
    private String originCodes; // VD: "HAN,SGN,DAD"

    @Column(name = "destination_codes", length = 255)
    private String destinationCodes; // VD: "NRT,KIX"

    @Column(name = "trip_type", length = 50)
    private String tripType; // VD: "flight", "train", "bus", "all"

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
