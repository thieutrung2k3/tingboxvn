package com.kir.notificationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.kir.commonservice.constant.AppConstants;
import com.kir.commonservice.dto.request.OtpRequest;
import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import com.kir.notificationservice.constant.Channel;
import com.kir.notificationservice.constant.ConfigConstant;
import com.kir.notificationservice.constant.NotificationType;
import com.kir.notificationservice.constant.VariableConstant;
import com.kir.notificationservice.entity.EmailTemplate;
import com.kir.notificationservice.entity.Notification;
import com.kir.notificationservice.entity.NotificationLog;
import com.kir.notificationservice.repository.EmailTemplateRepository;
import com.kir.notificationservice.repository.NotificationConfigRepository;
import com.kir.notificationservice.repository.NotificationLogRepository;
import com.kir.notificationservice.repository.NotificationRepository;
import com.kir.notificationservice.service.EmailSenderService;
import com.kir.notificationservice.service.OtpNotificationService;
import com.kir.notificationservice.util.FileUtil;
import com.kir.commonservice.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpNotificationServiceImpl implements OtpNotificationService {

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationConfigRepository notificationConfigRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public void sendEmailOtp(OtpRequest request) {
        NotificationLog log = new NotificationLog();

        try {
            if (Strings.isNullOrEmpty(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_NOT_NULL);
            }
            if (!OtpUtil.isValidOtp(request.getOtp())) {
                throw new AppException(ErrorCode.OTP_CODE_INVALID);
            }
            if (Strings.isNullOrEmpty(request.getOtp())) {
                request.setOtp(OtpUtil.generateOtp());
            }

            // Lưu notification
            Notification n = new Notification();
            n.setNotificationType(NotificationType.OTP);
            n.setChannel(Channel.EMAIL);
            n.setReferenceInfo(objectMapper.writeValueAsString(request.getCustomerInfo()));
            n = notificationRepository.save(n);

            // Khởi tạo log
            log.setNotificationId(n.getId());
            log.setCustomerId(request.getCustomerInfo().getCustomerId());
            log.setStatus(AppConstants.NotificationStatus.PENDING);

            // Lấy template
            String emailTemplateCode = notificationConfigRepository
                    .findEmailTemplateCodeByConfigKey(ConfigConstant.ConfigKey.EMAIL_TEMPLATE_CODE);
            log.setEmailTemplateCode(emailTemplateCode);

            EmailTemplate template = emailTemplateRepository.findByTemplateCode(emailTemplateCode)
                    .orElseThrow(() -> new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND));

            // Đọc nội dung template
            String content = FileUtil.readFile(template.getTemplatePath());
            if (Strings.isNullOrEmpty(content)) {
                throw new AppException(ErrorCode.EMAIL_TEMPLATE_NULL);
            }
            content = content.replace(VariableConstant.Otp.RECEIVER_NAME, request.getCustomerInfo().getCustomerName())
                    .replace(VariableConstant.Otp.OTP_CODE, request.getOtp());

            // Gửi mail
            emailSenderService.send(request.getEmail(), template.getSubject(), content);
            log.setStatus(AppConstants.NotificationStatus.SENT);

        } catch (AppException ex) {
            log.setStatus(AppConstants.NotificationStatus.FAILED);
            log.setDetail(ex.getErrorCode().getMessage());
            throw ex;
        } catch (Exception ex) {
            log.setStatus(AppConstants.NotificationStatus.FAILED);
            log.setDetail(ex.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        } finally {
            notificationLogRepository.save(log);
        }
    }

}
