package com.kir.notificationservice.service;

import com.kir.notificationservice.dto.request.OtpRequest;

public interface OtpNotificationService {
    void sendOtp(OtpRequest request);
}
