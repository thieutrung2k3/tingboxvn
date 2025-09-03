package com.kir.notificationservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import com.kir.notificationservice.constant.NotificationType;
import com.kir.notificationservice.dto.request.OtpRequest;
import com.kir.notificationservice.entity.Notification;
import com.kir.notificationservice.service.OtpNotificationService;
import com.kir.notificationservice.util.OtpUtil;
import jdk.internal.joptsimple.internal.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpNotificationServiceImpl implements OtpNotificationService {

    private final ObjectMapper objectMapper;

    @Override
    public void sendOtp(OtpRequest request) {
        if (Strings.isNullOrEmpty(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_NOT_NULL);
        }
        if (OtpUtil.isValidOtp(request.getOtp())) {
            throw new AppException(ErrorCode.OTP_CODE_INVALID);
        }
        if (Strings.isNullOrEmpty(request.getOtp())) {
            request.setOtp(OtpUtil.generateOtp());
        }
        Notification n = new Notification();
        n.setNotificationType(NotificationType.OTP);
        try {
            String customerInfo = objectMapper.writeValueAsString(request.getCustomerInfo());
            n.setReferenceInfo(customerInfo);
        } catch (Exception e) {
            throw new AppException(ErrorCode.JSON_PARSE_ERROR);


        }
    }
