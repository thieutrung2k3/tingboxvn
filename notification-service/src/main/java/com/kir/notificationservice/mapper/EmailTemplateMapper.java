package com.kir.notificationservice.mapper;

import com.kir.notificationservice.dto.request.EmailTemplateCreationRequest;
import com.kir.notificationservice.dto.response.EmailTemplateResponse;
import com.kir.notificationservice.entity.EmailTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailTemplateMapper {
    EmailTemplate toEmailTemplate(EmailTemplateCreationRequest request);

    EmailTemplateResponse toEmailTemplateResponse(EmailTemplate emailTemplate);
}
