package com.kir.notificationservice.service.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir.commonservice.cache.QueueMessage;
import com.kir.commonservice.config.RabbitMQConfig;
import com.kir.commonservice.constant.QueueConstant;
import com.kir.commonservice.dto.request.OtpRequest;
import com.kir.notificationservice.service.OtpNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final OtpNotificationService otpNotificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeMessage(String rawMessage) {
        try {
            QueueMessage<?> queueMessage = objectMapper.readValue(rawMessage, new TypeReference<QueueMessage<?>>() {
            });
            switch (queueMessage.getType()) {
                case QueueConstant.Type.OTP -> {
                    OtpRequest otpRequest = objectMapper.convertValue(queueMessage.getPayload(), OtpRequest.class);
                    otpNotificationService.sendEmailOtp(otpRequest);
                    log.info("[NOTIFICATION_CONSUMER]: Send email OTP successfully.");
                }
                default -> {
                    log.warn("[NOTIFICATION_CONSUMER]: Unknown message type: {}", queueMessage.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
