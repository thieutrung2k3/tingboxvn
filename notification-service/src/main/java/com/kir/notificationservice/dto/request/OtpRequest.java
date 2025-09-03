package com.kir.notificationservice.dto.request;

import com.kir.notificationservice.dto.CustomerInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpRequest {
    private CustomerInfo customerInfo;
    private String email;
    private String otp;
}
