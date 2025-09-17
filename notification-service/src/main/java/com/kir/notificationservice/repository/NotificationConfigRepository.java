package com.kir.notificationservice.repository;

import com.kir.notificationservice.entity.NotificationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface NotificationConfigRepository extends JpaRepository<NotificationConfig, Long> {
    @Query(value = "SELECT nc.config_value FROM notification_configs nc WHERE nc.config_key = ?1", nativeQuery = true)
    String findEmailTemplateCodeByConfigKey(String configKey);
}
