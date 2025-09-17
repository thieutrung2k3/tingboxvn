package com.kir.commonservice.dto.request;

public class CustomerInfo {
    private Long customerId;
    private String customerName;
    private String phoneNumber;


    public static class Builder {
        private final CustomerInfo ins = new CustomerInfo();

        public Builder setCustomerId(Long customerId) {
            ins.setCustomerId(customerId);
            return this;
        }

        public Builder setCustomerName(String customerName) {
            ins.setCustomerName(customerName);
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            ins.setPhoneNumber(phoneNumber);
            return this;
        }

        public CustomerInfo build() {
            return ins;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
