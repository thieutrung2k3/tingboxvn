-- SQL script để thêm template email OTP vào database
-- TingBox VN - Notification Service

-- 1. Insert email template vào bảng email_templates
INSERT INTO email_templates (
    template_code, 
    template_name, 
    subject, 
    template_path, 
    variables, 
    is_active, 
    is_delete, 
    created_at, 
    updated_at
) VALUES (
    'OTP_VERIFICATION',
    'Email xác thực OTP',
    'Mã xác thực OTP từ TingBox VN',
    'templates/email/OTP_VERIFICATION.html',
    ARRAY['{receiver_name}', '{otp_code}'],
    true,
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 2. Insert config để map template code với config key
INSERT INTO notification_configs (
    config_key,
    config_value,
    description,
    is_delete,
    created_at,
    updated_at
) VALUES (
    'EMAIL_TEMPLATE_CODE',
    'OTP_VERIFICATION',
    'Template code cho email gửi OTP xác thực tài khoản',
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- 3. Kiểm tra dữ liệu đã được insert
SELECT 
    et.id,
    et.template_code,
    et.template_name,
    et.subject,
    et.template_path,
    et.is_active
FROM email_templates et 
WHERE et.template_code = 'OTP_VERIFICATION';

SELECT 
    nc.id,
    nc.config_key,
    nc.config_value,
    nc.description
FROM notification_configs nc 
WHERE nc.config_key = 'EMAIL_TEMPLATE_CODE';

-- 4. Query để test việc lấy template theo config (giống như trong code)
SELECT et.* 
FROM email_templates et
JOIN notification_configs nc ON et.template_code = nc.config_value
WHERE nc.config_key = 'EMAIL_TEMPLATE_CODE'
  AND et.is_active = true 
  AND et.is_delete = false;