CREATE TABLE available_seats
(
    id               SERIAL PRIMARY KEY,
    trip_id          INTEGER REFERENCES trips (id),
    seat_number      VARCHAR(20) NOT NULL,
    seat_class       VARCHAR(50),                           -- 'ECONOMY', 'BUSINESS', 'FIRST'
    price_adjustment DECIMAL(10, 2)              DEFAULT 0, -- Phần bù giá theo loại ghế
    is_available     BOOLEAN                     DEFAULT TRUE,
    created_at       TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (trip_id, seat_number)
);

CREATE TABLE seat_maps
(
    id            BIGSERIAL PRIMARY KEY,
    trip_id       INTEGER REFERENCES trips (id),
    vehicle_type  VARCHAR(50) NOT NULL, -- 'AIRPLANE', 'TRAIN', 'BUS'
    vehicle_model VARCHAR(100),         -- Mẫu phương tiện, ví dụ: 'Boeing 787', 'SE Train'
    rows          INTEGER     NOT NULL, -- Số hàng
    columns       INTEGER     NOT NULL, -- Số cột
    layout_data   JSONB,                -- Dữ liệu sơ đồ ghế dạng JSON
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE seat_reservations
(
    id          BIGSERIAL PRIMARY KEY,
    trip_id     INTEGER REFERENCES trips (id),
    seat_number VARCHAR(20)                 NOT NULL,
    session_id  VARCHAR(255)                NOT NULL, -- ID phiên của người dùng
    expiry_time TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- Thời gian hết hạn của việc giữ ghế
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (trip_id, seat_number)
);
