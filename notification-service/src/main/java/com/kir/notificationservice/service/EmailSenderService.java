package com.kir.notificationservice.service;

public interface EmailSenderService {
    void send(String to, String subject, String body);
}
