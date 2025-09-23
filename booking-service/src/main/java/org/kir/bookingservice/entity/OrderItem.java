package org.kir.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "item_type", nullable = false, length = 50)
    private String itemType;

    @Column(name = "item_reference_id", nullable = false, length = 255)
    private String itemReferenceId;

    @Column(name = "departure_location_code", nullable = false, length = 10)
    private String departureLocationCode;

    @Column(name = "arrival_location_code", nullable = false, length = 10)
    private String arrivalLocationCode;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "seat_number", length = 20)
    private String seatNumber;

    @Column(name = "is_delete")
    private Boolean isDelete;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<Passenger> passengers;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


