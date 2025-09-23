package org.kir.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_changes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "change_type", nullable = false, length = 50)
    private String changeType;

    @Column(name = "original_data", columnDefinition = "jsonb")
    private String originalData;

    @Column(name = "new_data", columnDefinition = "jsonb")
    private String newData;

    @Column(name = "fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "change_date")
    private LocalDateTime changeDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


