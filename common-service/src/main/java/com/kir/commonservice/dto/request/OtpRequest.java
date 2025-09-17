package com.kir.commonservice.dto.request;

public class OtpRequest {
    private CustomerInfo customerInfo;
    private String email;
    private String otp;

    public static class Builder {
        private final OtpRequest ins = new OtpRequest();

        public Builder setCustomerInfo(CustomerInfo customerInfo) {
            ins.setCustomerInfo(customerInfo);
            return this;
        }

        public Builder setEmail(String email) {
            ins.setEmail(email);
            return this;
        }

        public Builder setOtp(String otp) {
            ins.setOtp(otp);
            return this;
        }

        public OtpRequest build() {
            return ins;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
