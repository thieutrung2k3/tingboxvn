package com.kir.notificationservice.entity;

import com.kir.notificationservice.constant.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notification_preferences")
public class NotificationPreference {
    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "enable_trip_reminders")
    private Boolean enableTripReminders = true;

    @Column(name = "enable_promotion")
    private Boolean enablePromotion = true;

    @Column(name = "max_reminders_per_day")
    private Integer maxRemindersPerDay = 5;

    @Column(name = "quiet_hours_start")
    private LocalTime quietHoursStart = LocalTime.of(22, 0);

    @Column(name = "quiet_hours_end")
    private LocalTime quietHoursEnd = LocalTime.of(8, 0);

    @ElementCollection
    @CollectionTable(name = "unsubscribed_types", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private List<NotificationType> unsubscribedTypes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

