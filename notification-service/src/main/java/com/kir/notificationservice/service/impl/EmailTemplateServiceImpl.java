package com.kir.notificationservice.service.impl;

import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import com.kir.notificationservice.dto.request.EmailTemplateCreationRequest;
import com.kir.notificationservice.dto.request.EmailTemplateUpdateRequest;
import com.kir.notificationservice.dto.response.EmailTemplateResponse;
import com.kir.notificationservice.entity.EmailTemplate;
import com.kir.notificationservice.mapper.EmailTemplateMapper;
import com.kir.notificationservice.repository.EmailTemplateRepository;
import com.kir.notificationservice.service.EmailTemplateService;
import com.kir.notificationservice.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {
    private static final String TEMPLATE_PATH = "./templates/email/%s.html";
    //Repository
    private final EmailTemplateRepository emailTemplateRepository;
    //Mapper
    private final EmailTemplateMapper emailTemplateMapper;


    @Override
    public void createEmailTemplate(EmailTemplateCreationRequest request) {
        if(emailTemplateRepository.existsByTemplateCode(request.getTemplateCode())){
            throw new AppException(ErrorCode.EMAIL_TEMPLATE_EXISTED);
        }
        EmailTemplate emailTemplate = emailTemplateMapper.toEmailTemplate(request);

        String templatePath = String.format(TEMPLATE_PATH, emailTemplate.getTemplateCode());
        emailTemplate.setTemplatePath(templatePath);

        FileUtil.saveFile(templatePath, request.getContent());
        try {
            emailTemplateRepository.save(emailTemplate);
            log.info("Email template created successfully.");
        } catch (AppException e) {
            throw new AppException(ErrorCode.CAN_NOT_CREATE_EMAIL_TEMPLATE);
        }
    }

    @Override
    public void updateEmailTemplate(EmailTemplateUpdateRequest request, Long id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        emailTemplate.setTemplateCode(request.getTemplateCode());
        emailTemplate.setTemplateName(request.getTemplateName());
        emailTemplate.setSubject(request.getSubject());
        emailTemplate.setVariables(request.getVariables());
        String templatePath = String.format(TEMPLATE_PATH, emailTemplate.getTemplateCode());
        emailTemplate.setTemplatePath(templatePath);

        FileUtil.saveFile(templatePath, request.getContent());

        emailTemplateRepository.save(emailTemplate);
        log.info("Email template updated successfully.");
    }

    @Override
    public void softDeleteEmailTemplate(Long id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));
        emailTemplate.setIsDelete(true);
    }

    @Override
    public void hardDeleteEmailTemplate(Long id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        //Delete file
        emailTemplateRepository.delete(emailTemplate);
        FileUtil.deleteFile(emailTemplate.getTemplatePath());
    }

    @Override
    public void restoreEmailTemplate(Long id) {
        EmailTemplate emailTemplate = emailTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

        if (!emailTemplate.getIsDelete()) {
            throw new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_DELETED);
        }

        emailTemplate.setIsDelete(false);
        emailTemplateRepository.save(emailTemplate);
        log.info("Email template restored successfully.");
    }

    @Override
    public List<EmailTemplateResponse> getAllEmailTemplates() {
        return emailTemplateRepository.findAll().stream()
                .filter(emailTemplate -> !emailTemplate.getIsDelete())
                .map(emailTemplateMapper::toEmailTemplateResponse).collect(Collectors.toList());
    }

    @Override
    public List<EmailTemplateResponse> getEmailTemplatesSoftDeleted(Boolean isActive) {
        return emailTemplateRepository.findAll().stream()
                .filter(EmailTemplate::getIsDelete)
                .map(emailTemplateMapper::toEmailTemplateResponse).collect(Collectors.toList());
    }

    @Override
    public EmailTemplateResponse getEmailTemplateById(Long id) {
        return emailTemplateMapper.toEmailTemplateResponse(emailTemplateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND)));
    }

    @Override
    public String renderTemplate(String templateCode, Map<String, Object> data) {
        String templatePath = String.format(TEMPLATE_PATH, templateCode);
        try{
            String content = Files.readString(Paths.get(templatePath));
            for(Map.Entry<String, Object> entry : data.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue().toString());
            }
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
