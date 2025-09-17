package com.kir.notificationservice.entity;

import com.kir.notificationservice.constant.ScheduleStatus;
import com.kir.notificationservice.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notification_schedules")
public class NotificationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_id", nullable = false)
    private Long notificationId;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Column(name = "stop_condition", columnDefinition = "TEXT")
    private String stopCondition;

    @Column(name = "auto_cancel_after")
    private LocalDateTime autoCancelAfter;

    @Column(name = "cancelled_reason")
    private String cancelledReason;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
