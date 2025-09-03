package com.kir.notificationservice.controller;

import com.kir.commonservice.dto.ApiResponse;
import com.kir.commonservice.exception.AppException;
import com.kir.commonservice.exception.ErrorCode;
import com.kir.notificationservice.dto.request.EmailTemplateCreationRequest;
import com.kir.notificationservice.dto.request.EmailTemplateUpdateRequest;
import com.kir.notificationservice.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email-template")
@RequiredArgsConstructor
public class EmailTemplateController {
    private final EmailTemplateService emailTemplateService;

    @PostMapping("/admin/create")
    public ApiResponse<String> createEmailTemplate(@RequestBody EmailTemplateCreationRequest request){
        emailTemplateService.createEmailTemplate(request);
        return ApiResponse.success("Email template created successfully.");
    }

    @PutMapping("/admin/update/{id}")
    public ApiResponse<String> updateEmailTemplate(@RequestBody EmailTemplateUpdateRequest request, @PathVariable Long id){
        emailTemplateService.updateEmailTemplate(request, id);
        return ApiResponse.success("Email template updated successfully.");
    }

    @PutMapping("/admin/softDelete/{id}")
    public ApiResponse<String> softDeleteEmailTemplate(@PathVariable Long id){
        emailTemplateService.softDeleteEmailTemplate(id);
        return ApiResponse.success("Email template deleted successfully.");
    }

    @DeleteMapping("/admin/hardDelete/{id}")
    public ApiResponse<String> hardDeleteEmailTemplate(@PathVariable Long id){
        emailTemplateService.hardDeleteEmailTemplate(id);
        return ApiResponse.success("Email template deleted successfully.");
    }

    @PutMapping("/admin/restore/{id}")
    public ApiResponse<String> restoreEmailTemplate(@PathVariable Long id){
        emailTemplateService.restoreEmailTemplate(id);
        return ApiResponse.success("Email template restored successfully.");
    }

    @GetMapping("/public/test")
    public ApiResponse<String> test(){
        throw new AppException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
    }
}
