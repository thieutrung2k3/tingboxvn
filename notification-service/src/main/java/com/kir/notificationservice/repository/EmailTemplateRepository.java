package com.kir.notificationservice.repository;

import com.kir.notificationservice.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    boolean existsByTemplateCode(String templateCode);
}
