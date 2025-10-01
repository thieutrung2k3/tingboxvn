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
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber; // e.g., "1A", "12B", "25C"

    @Column(name = "seat_class", nullable = false)
    private String seatClass; // e.g., "ECONOMY", "BUSINESS", "FIRST"

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(name = "column_letter", nullable = false)
    private String columnLetter; // e.g., "A", "B", "C", "D"

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Column(name = "price_modifier")
    private Double priceModifier = 0.0; // Additional cost for this seat

    @Column(name = "is_window_seat")
    private Boolean isWindowSeat = false;

    @Column(name = "is_aisle_seat")
    private Boolean isAisleSeat = false;

    @Column(name = "is_emergency_exit")
    private Boolean isEmergencyExit = false;

    @Column(name = "is_delete")
    @Builder.Default
    private Boolean isDelete = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum SeatStatus {
        AVAILABLE,
        OCCUPIED,
        RESERVED,
        BLOCKED
    }
}

