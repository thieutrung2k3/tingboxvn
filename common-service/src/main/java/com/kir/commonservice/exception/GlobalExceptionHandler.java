package com.kir.commonservice.exception;

import com.kir.commonservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {
        ErrorCode ErrorCode = e.getErrorCode();
        return buildResponse(ErrorCode);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse> handleNoSuchElementException(NoSuchElementException e) {
        return buildResponse(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return buildResponse(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException e) {
        return buildResponse(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleJsonParseError(HttpMessageNotReadableException e) {
        return buildResponse(ErrorCode.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationFailed(MethodArgumentNotValidException e) {
        return buildResponse(ErrorCode.VALIDATION_FAILED);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleFileTooLarge(MaxUploadSizeExceededException e) {
        return buildResponse(ErrorCode.FILE_TOO_LARGE);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse> handleDatabaseConnectionError(SQLException e) {
        return buildResponse(ErrorCode.DATABASE_CONNECTION_ERROR);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleOtherExceptions(Exception e) {
//        return buildResponse(ErrorCode.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNullPointerException(NullPointerException e) {
        return buildResponse(ErrorCode.NULL_POINTER);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ApiResponse> handleNumberFormatException(NumberFormatException e) {
        return buildResponse(ErrorCode.PARSE_ERROR);
    }

    private ResponseEntity<ApiResponse> buildResponse(ErrorCode ErrorCode) {
        return ResponseEntity.status(ErrorCode.getHttpStatus())
                .body(ApiResponse.error(ErrorCode));
    }
}

