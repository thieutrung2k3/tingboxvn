CREATE TABLE notifications (
    id                BIGSERIAL PRIMARY KEY,
    reference_id      BIGINT, -- order_id, ticket_id, etc.
    notification_type VARCHAR(50) NOT NULL,
    message           TEXT        NOT NULL,
    channel           VARCHAR(50) NOT NULL,
    is_delete         BOOLEAN DEFAULT FALSE,
    is_broadcast      BOOLEAN DEFAULT FALSE,
    created_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recipients (
    id              BIGSERIAL PRIMARY KEY,
    notification_id BIGINT REFERENCES notifications (id),
    customer_id     BIGINT NOT NULL,
    status          VARCHAR(50) NOT NULL,
    sent_at         TIMESTAMP WITHOUT TIME ZONE,
    read_at         TIMESTAMP WITHOUT TIME ZONE,
    error_message   TEXT,
    retry_count     INTEGER DEFAULT 0,
    max_retries     INTEGER DEFAULT 3,
    next_retry_at   TIMESTAMP WITH TIME ZONE,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notification_schedules (
    id                 BIGSERIAL PRIMARY KEY,
    notification_id    BIGINT NOT NULL REFERENCES notifications (id),
    scheduled_time     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    stop_condition     TEXT,
    auto_cancel_after  TIMESTAMP WITHOUT TIME ZONE,
    cancelled_reason   VARCHAR(255),
    status             VARCHAR(50) NOT NULL,
    is_delete          BOOLEAN DEFAULT FALSE,
    created_at         TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notification_preferences (
    customer_id            BIGINT PRIMARY KEY,
    enable_trip_reminders  BOOLEAN DEFAULT TRUE,
    enable_promotion       BOOLEAN DEFAULT TRUE,
    max_reminders_per_day  INTEGER DEFAULT 5,
    quiet_hours_start      TIME DEFAULT '22:00:00',
    quiet_hours_end        TIME DEFAULT '08:00:00',
    unsubscribed_types     TEXT[],
    created_at             TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE email_templates (
    id            BIGSERIAL PRIMARY KEY,
    template_code VARCHAR(50) UNIQUE NOT NULL,
    template_name VARCHAR(255)       NOT NULL,
    subject       VARCHAR(255)       NOT NULL,
    template_path VARCHAR(255)       NOT NULL,
    variables     TEXT[],
    is_active     BOOLEAN DEFAULT TRUE,
    is_delete     BOOLEAN DEFAULT FALSE,
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notification_logs (
    id              BIGSERIAL PRIMARY KEY,
    notification_id BIGINT REFERENCES notifications (id),
    customer_id     BIGINT NOT NULL,
    template_id     BIGINT REFERENCES email_templates (id),
    action          VARCHAR(50) NOT NULL,
    details         JSONB,
    ip_address      INET,
    user_agent      TEXT,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- INDEXES FOR PERFORMANCE
-- ========================================

-- Indexes cho notifications
CREATE INDEX idx_notifications_order_id ON notifications (reference_id);
CREATE INDEX idx_notifications_type ON notifications (notification_type);
CREATE INDEX idx_notifications_broadcast ON notifications (is_broadcast);
CREATE INDEX idx_notifications_created_at ON notifications (created_at);

-- Indexes cho notification_recipients
CREATE INDEX idx_notification_recipients_notification_id ON recipients (notification_id);
CREATE INDEX idx_notification_recipients_customer_id ON recipients (customer_id);
CREATE INDEX idx_notification_recipients_status ON recipients (status);
CREATE INDEX idx_notification_recipients_retry ON recipients (status, next_retry_at) WHERE status = 'PENDING';
CREATE INDEX idx_notification_recipients_sent_at ON recipients (sent_at);

-- Indexes cho notification_schedules
CREATE INDEX idx_notification_schedules_notification_id ON notification_schedules (notification_id);
CREATE INDEX idx_notification_schedules_scheduled_time ON notification_schedules (scheduled_time);
CREATE INDEX idx_notification_schedules_status ON notification_schedules (status);
CREATE INDEX idx_notification_schedules_auto_cancel ON notification_schedules (status, auto_cancel_after) WHERE status = 'PENDING';

-- Indexes cho notification_logs
CREATE INDEX idx_notification_logs_notification_id ON notification_logs (notification_id);
CREATE INDEX idx_notification_logs_customer_id ON notification_logs (customer_id);
CREATE INDEX idx_notification_logs_action ON notification_logs (action);
CREATE INDEX idx_notification_logs_created_at ON notification_logs (created_at);