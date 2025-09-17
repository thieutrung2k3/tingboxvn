package com.kir.commonservice.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa xác định.", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * 999. Unauthenticated
     */
    UNAUTHENTICATED(9990, "Chưa được xác thực.", HttpStatus.UNAUTHORIZED),
    OTP_INVALID(9991, "Mã OTP không hợp lệ.", HttpStatus.UNAUTHORIZED),
    SERVICE_UNAVAILABLE(9992, "Dịch vụ không có sẵn.", HttpStatus.SERVICE_UNAVAILABLE),

    /**
     * 1. Account
     */
    USER_PASSWORD_ERROR(1001, "Mật khẩu tài khoản không chính xác.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "Tài khoản không tồn tại.", HttpStatus.NOT_FOUND),
    USER_SESSION_EXPIRED(1003, "Phiên đăng nhập của tài khoản đã hết hạn, vui lòng đăng nhập lại.", HttpStatus.UNAUTHORIZED),
    USER_NOT_ACTIVATED(1004, "Tài khoản chưa được kích hoạt.", HttpStatus.FORBIDDEN),
    EMAIL_EXISTED(1005, "Emai đã tồn tại.", HttpStatus.BAD_REQUEST),
    REGISTRATION_FAILED(1006, "Đăng kí thất bại.", HttpStatus.BAD_REQUEST),
    USER_ACTIVATED(1007, "Tài khoản đã được kích hoạt.", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1008, "Vai trò không tồn tại.", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(1009, "Quyền không tồn tại.", HttpStatus.NOT_FOUND),
    USER_DELETED(1010, "Tài khoản đã bị xóa.", HttpStatus.BAD_REQUEST),
    /**
     * 2. Notification
     */
    //Error
    EMAIL_TEMPLATE_NOT_FOUND(2001, "Email template không tồn tại.", HttpStatus.NOT_FOUND),
    CAN_NOT_SAVE_FILE(2002, "Có lỗi xảy ra khi lưu file.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND(2003, "File không tồn tại.", HttpStatus.NOT_FOUND),
    EMAIL_TEMPLATE_NOT_DELETED(2003, "Email template không bị xoá, không cần khôi phục.", HttpStatus.BAD_REQUEST),
    EMAIL_TEMPLATE_EXISTED(2004, "Email template đã tồn tại.", HttpStatus.BAD_REQUEST),
    CAN_NOT_CREATE_EMAIL_TEMPLATE(2005, "Có lỗi xảy ra khi tạo email template", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_NULL(2006, "Email không được bỏ trống.", HttpStatus.BAD_REQUEST),
    OTP_CODE_INVALID(2007, "Mã OTP không hợp lệ.", HttpStatus.BAD_REQUEST),
    EMAIL_TEMPLATE_NULL(2008, "Email template rỗng.", HttpStatus.BAD_REQUEST),
    EMAIL_SEND_FAILED(2009, "Gửi email thất bại.", HttpStatus.BAD_REQUEST),
    /**
     * 3. Search
     */
    JSON_WRITE_ERROR(3001, "Xảy ra lỗi Json Write Error.", HttpStatus.BAD_REQUEST),

    /**
     * 000. Common
     */
    DATA_INTEGRITY_VIOLATION(9901, "Dữ liệu vi phạm ràng buộc toàn vẹn.", HttpStatus.CONFLICT),
    RESOURCE_NOT_FOUND(9902, "Không tìm thấy tài nguyên.", HttpStatus.NOT_FOUND),
    BAD_REQUEST(9903, "Yêu cầu không hợp lệ.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(9904, "Không được phép truy cập.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(9905, "Bạn không có quyền thực hiện hành động này.", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR(9906, "Đã xảy ra lỗi nội bộ.", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED(9907, "Phương thức không được hỗ trợ.", HttpStatus.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE(9908, "Định dạng dữ liệu không được hỗ trợ.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    REQUEST_TIMEOUT(9909, "Yêu cầu quá thời gian chờ.", HttpStatus.REQUEST_TIMEOUT),
    CONFLICT(9910, "Xung đột dữ liệu.", HttpStatus.CONFLICT),
    VALIDATION_FAILED(9911, "Dữ liệu không hợp lệ.", HttpStatus.BAD_REQUEST),
    JSON_PARSE_ERROR(9912, "Lỗi phân tích JSON.", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(9913, "Tập tin vượt quá kích thước cho phép.", HttpStatus.PAYLOAD_TOO_LARGE),
    DATABASE_CONNECTION_ERROR(9914, "Không thể kết nối tới cơ sở dữ liệu.", HttpStatus.SERVICE_UNAVAILABLE),
    PARSE_ERROR(9915, "Dữ liệu đầu vào không đúng định dạng số.", HttpStatus.BAD_REQUEST),
    NULL_POINTER(9916, "Đã xảy ra lỗi hệ thống (NullPointer).", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * 900. Gateway Errors
     */
    GATEWAY_UNAUTHORIZED(9000, "GATEWAY: Không có quyền truy cập.", HttpStatus.UNAUTHORIZED),
    GATEWAY_UNAUTHENTICATED(9001, "GATEWAY: Chưa xác thực người dùng.", HttpStatus.UNAUTHORIZED),
    GATEWAY_ROUTE_NOT_FOUND(9002, "GATEWAY: Không tìm thấy route tương ứng.", HttpStatus.NOT_FOUND),
    GATEWAY_FILTER_ERROR(9003, "GATEWAY: Lỗi trong quá trình xử lý filter.", HttpStatus.INTERNAL_SERVER_ERROR),
    GATEWAY_TIMEOUT(9004, "GATEWAY: Không nhận được phản hồi từ dịch vụ đích (timeout).", HttpStatus.GATEWAY_TIMEOUT),
    GATEWAY_INVALID_TOKEN(9005, "GATEWAY: Token không hợp lệ hoặc đã bị chỉnh sửa.", HttpStatus.UNAUTHORIZED),
    GATEWAY_TOKEN_EXPIRED(9006, "GATEWAY: Token đã hết hạn.", HttpStatus.UNAUTHORIZED),
    GATEWAY_REQUEST_TOO_LARGE(9007, "GATEWAY: Request vượt quá kích thước tối đa cho phép.", HttpStatus.PAYLOAD_TOO_LARGE),
    GATEWAY_UNSUPPORTED_ENCODING(9008, "GATEWAY: Encoding của request không được hỗ trợ.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    GATEWAY_SERVICE_UNREGISTERED(9009, "GATEWAY: Dịch vụ chưa được đăng ký trong hệ thống Discovery.", HttpStatus.SERVICE_UNAVAILABLE),
    GATEWAY_RATE_LIMITED(9010, "GATEWAY: Đã vượt quá giới hạn truy cập cho phép (Rate Limit).", HttpStatus.TOO_MANY_REQUESTS),

    ;


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
