-- event-service.sql
-- Bảng quản lý sự kiện
CREATE TABLE events
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    event_type       VARCHAR(50)  NOT NULL, -- 'HOLIDAY', 'SEASONAL', 'FLASH_SALE', 'LAUNCH'
    start_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    banner_image_url VARCHAR(255),
    is_active        BOOLEAN DEFAULT TRUE,
    is_delete        BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Bảng liên kết giữa sự kiện và khuyến mãi
CREATE TABLE event_promotions
(
    id           BIGSERIAL PRIMARY KEY,
    event_id     BIGINT REFERENCES events (id),
    promotion_id BIGINT REFERENCES promotions (id),
    is_delete    BOOLEAN DEFAULT FALSE,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Bảng quy tắc áp dụng cho sự kiện
CREATE TABLE event_rules
(
    id         BIGSERIAL PRIMARY KEY,
    event_id   BIGINT REFERENCES events (id),
    rule_type  VARCHAR(50) NOT NULL, -- 'ELIGIBILITY', 'STACKABLE', 'PRIORITY'
    rule_value TEXT        NOT NULL,
    is_delete  BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Bảng đối tượng mục tiêu của sự kiện
CREATE TABLE event_targets
(
    id           BIGSERIAL PRIMARY KEY,
    event_id     BIGINT REFERENCES events (id),
    target_type  VARCHAR(50) NOT NULL, -- 'ALL', 'CUSTOMER_SEGMENT', 'ROUTE', 'PRODUCT_TYPE'
    target_value TEXT        NOT NULL,
    is_delete    BOOLEAN DEFAULT FALSE,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE promotions
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    description    TEXT,
    promotion_type VARCHAR(50)  NOT NULL, -- 'VOUCHER', 'DISCOUNT', 'CASHBACK', 'BOGO'
    start_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_active      BOOLEAN DEFAULT TRUE,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vouchers
(
    id              BIGSERIAL PRIMARY KEY,
    promotion_id    BIGINT REFERENCES promotions (id),
    code            VARCHAR(50) UNIQUE NOT NULL,
    discount_type   VARCHAR(20)        NOT NULL, -- 'PERCENTAGE', 'FIXED_AMOUNT'
    discount_value  DECIMAL(10, 2)     NOT NULL,
    min_order_value DECIMAL(10, 2) DEFAULT 0,
    max_discount    DECIMAL(10, 2),
    usage_limit     INTEGER,
    usage_count     INTEGER        DEFAULT 0,
    is_delete       BOOLEAN        DEFAULT FALSE,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE voucher_conditions
(
    id              BIGSERIAL PRIMARY KEY,
    voucher_id      BIGINT REFERENCES vouchers (id) ON DELETE CASCADE,
    condition_type  VARCHAR(50) NOT NULL, -- 'CUSTOMER_TYPE', 'TRIP_TYPE', 'LOCATION', 'TIME'
    condition_value TEXT        NOT NULL,
    is_delete       BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE voucher_usages
(
    id              BIGSERIAL PRIMARY KEY,
    voucher_id      BIGINT REFERENCES vouchers (id),
    order_id        BIGINT         NOT NULL,
    customer_id     BIGINT         NOT NULL,
    discount_amount DECIMAL(10, 2) NOT NULL,
    is_delete       BOOLEAN DEFAULT FALSE,
    used_at         TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);