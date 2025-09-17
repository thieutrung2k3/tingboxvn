package com.kir.notificationservice.service;

import com.kir.commonservice.dto.request.OtpRequest;

public interface OtpNotificationService {
    void sendEmailOtp(OtpRequest request);
}
