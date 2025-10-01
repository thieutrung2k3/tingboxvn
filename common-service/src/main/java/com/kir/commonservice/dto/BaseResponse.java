package com.kir.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kir.commonservice.util.CorrelationIdUtil;
import org.slf4j.MDC;

import java.time.LocalDateTime;

public abstract class BaseResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    private String correlationId = CorrelationIdUtil.get();

    // Getters and setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
}
