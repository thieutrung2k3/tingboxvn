-- Cập nhật cấu trúc bảng tickets
CREATE TABLE tickets
(
    id             BIGSERIAL PRIMARY KEY,
    order_item_id  INTEGER REFERENCES order_items (id) UNIQUE NOT NULL,
    ticket_code    VARCHAR(255) UNIQUE                        NOT NULL,
    passenger_id   INTEGER                                    NOT NULL,
    qr_code_url    VARCHAR(255), -- URL lưu trữ QR code
    qr_code_data   TEXT,         -- Dữ liệu được mã hóa trong QR code
    pdf_ticket_url VARCHAR(255), -- URL của file PDF vé điện tử
    is_checked_in  BOOLEAN DEFAULT FALSE,
    issue_date     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_delete      BOOLEAN DEFAULT FALSE,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);