CREATE TABLE payments
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT REFERENCES orders (id) UNIQUE NOT NULL,
    transaction_id VARCHAR(255) UNIQUE                   NOT NULL, -- Mã giao dịch từ cổng thanh toán
    payment_method VARCHAR(50)                           NOT NULL,
    payment_date   TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    amount         DECIMAL(10, 2)                        NOT NULL,
    status         VARCHAR(50)                           NOT NULL, -- 'PENDING', 'SUCCESS', 'FAILED'
    -- Các thông tin khác về thanh toán
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE provider_payments
(
    id                    BIGSERIAL PRIMARY KEY,
    provider_id           BIGINT,
    period_start          DATE           NOT NULL,
    period_end            DATE           NOT NULL,
    total_amount          DECIMAL(10, 2) NOT NULL,
    commission_amount     DECIMAL(10, 2) NOT NULL,
    net_amount            DECIMAL(10, 2) NOT NULL,
    payment_status        VARCHAR(50)    NOT NULL, -- 'PENDING', 'PROCESSED', 'FAILED'
    transaction_reference VARCHAR(255),
    payment_date          TIMESTAMP WITHOUT TIME ZONE,
    created_at            TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_distributions
(
    id                  BIGSERIAL PRIMARY KEY,
    payment_id          BIGINT REFERENCES payments (id),
    order_item_id       BIGINT,
    provider_id         BIGINT,
    amount              DECIMAL(10, 2) NOT NULL,
    commission_amount   DECIMAL(10, 2) NOT NULL,
    provider_amount     DECIMAL(10, 2) NOT NULL,
    is_settled          BOOLEAN DEFAULT FALSE,
    provider_payment_id BIGINT REFERENCES provider_payments (id),
    created_at          TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);