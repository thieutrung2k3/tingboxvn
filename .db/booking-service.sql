CREATE TABLE orders
(
    id             BIGSERIAL PRIMARY KEY,
    customer_id    BIGINT             NOT NULL, -- Tham chiếu đến `customer-service`
    order_code     VARCHAR(50) UNIQUE NOT NULL,
    order_date     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    total_amount   DECIMAL(10, 2)     NOT NULL,
    payment_status VARCHAR(50)        NOT NULL, -- 'PENDING', 'PAID', 'FAILED', 'REFUNDED'
    booking_status VARCHAR(50)        NOT NULL, -- 'CREATED', 'CONFIRMED', 'CANCELLED'
    is_delete      BOOLEAN                     DEFAULT FALSE,
    -- Các thông tin khác về đơn hàng
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items
(
    id                      BIGSERIAL PRIMARY KEY,
    order_id                BIGINT REFERENCES orders (id),
    item_type               VARCHAR(50)                 NOT NULL, -- 'flight', 'train', 'bus'
    item_reference_id       VARCHAR(255)                NOT NULL, -- ID tham chiếu đến thông tin chuyến đi cụ thể (có thể từ `search-service` hoặc API nhà cung cấp)
    departure_location_code VARCHAR(10)                 NOT NULL,
    arrival_location_code   VARCHAR(10)                 NOT NULL,
    departure_time          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    arrival_time            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    price                   DECIMAL(10, 2)              NOT NULL,
    seat_number             VARCHAR(20),                          -- Nếu có chọn chỗ
    is_delete               BOOLEAN                     DEFAULT FALSE,
    -- Các thông tin khác liên quan đến từng item trong đơn hàng
    created_at              TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE passengers
(
    id                       BIGSERIAL PRIMARY KEY,
    order_item_id            BIGINT REFERENCES order_items (id),
    first_name               VARCHAR(255) NOT NULL,
    last_name                VARCHAR(255) NOT NULL,
    date_of_birth            DATE,
    gender                   VARCHAR(10),
    identity_document_type   VARCHAR(50), -- 'ID_CARD', 'PASSPORT'
    identity_document_number VARCHAR(50)  NOT NULL,
    is_delete                BOOLEAN                     DEFAULT FALSE,
    -- Các thông tin khác về hành khách
    created_at               TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE booking_changes
(
    id            BIGSERIAL PRIMARY KEY,
    order_id      BIGINT REFERENCES orders (id),
    change_type   VARCHAR(50)    NOT NULL,               -- 'CANCEL', 'RESCHEDULE', 'SEAT_CHANGE'
    original_data JSONB,                                 -- Dữ liệu gốc trước khi thay đổi
    new_data      JSONB,                                 -- Dữ liệu mới sau khi thay đổi
    fee           DECIMAL(10, 2) NOT NULL     DEFAULT 0, -- Phí thay đổi
    status        VARCHAR(50)    NOT NULL,               -- 'PENDING', 'COMPLETED', 'REJECTED'
    change_date   TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


