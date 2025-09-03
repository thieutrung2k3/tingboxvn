package com.kir.commonservice.dto;

import com.kir.commonservice.exception.ErrorCode;

public class ApiResponse<T> extends BaseResponse {

    private boolean success = true;
    private int code = 200;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(boolean success, String message, int code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public static <T> ApiResponse<T> data(T data) {
        return new ApiResponse<T>(data);
    }
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getMessage(), errorCode.getCode());
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
