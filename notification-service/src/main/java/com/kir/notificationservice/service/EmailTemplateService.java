package com.kir.notificationservice.service;

import com.kir.notificationservice.dto.request.EmailTemplateCreationRequest;
import com.kir.notificationservice.dto.request.EmailTemplateUpdateRequest;
import com.kir.notificationservice.dto.response.EmailTemplateResponse;

import java.util.List;
import java.util.Map;

public interface EmailTemplateService {
    void createEmailTemplate(EmailTemplateCreationRequest request);

    void updateEmailTemplate(EmailTemplateUpdateRequest request, Long id);

    void softDeleteEmailTemplate(Long id);

    void hardDeleteEmailTemplate(Long id);

    void restoreEmailTemplate(Long id);

    List<EmailTemplateResponse> getAllEmailTemplates();

    List<EmailTemplateResponse> getEmailTemplatesSoftDeleted(Boolean isActive);

    EmailTemplateResponse getEmailTemplateById(Long id);

    String renderTemplate(String templateCode, Map<String, Object> data);
}
