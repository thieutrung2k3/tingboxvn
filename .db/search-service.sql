-- Bảng lưu trữ thông tin cơ bản về nhà cung cấp (nếu cần)
CREATE TABLE providers
(
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(255) UNIQUE NOT NULL,
    api_endpoint         VARCHAR(255),
    payment_account_info JSONB,         -- Thông tin tài khoản thanh toán
    commission_rate      DECIMAL(5, 2), -- Tỷ lệ hoa hồng (%)
    payment_schedule     VARCHAR(50),   -- Lịch thanh toán: 'DAILY', 'WEEKLY', 'MONTHLY'
    currency             VARCHAR(10)    -- Đơn vị tiền tệ thanh toán
);

-- Bảng lưu trữ thông tin cơ bản về địa điểm (sân bay, nhà ga, bến xe) (nếu cần cache)
CREATE TABLE locations
(
    id       BIGSERIAL PRIMARY KEY,
    iata     VARCHAR(10) UNIQUE NOT NULL, -- Mã IATA (3 ký tự)
    icao     VARCHAR(10),                 -- Mã ICAO (4 ký tự)
    name     VARCHAR(255)       NOT NULL, -- Tên sân bay
    location VARCHAR(255),                -- Vị trí (thành phố, quốc gia)
    time     VARCHAR(20)                  -- Múi giờ (UTC±)
);


-- Bảng lưu trữ thông tin chuyến đi
CREATE TABLE trips
(
    id                BIGSERIAL PRIMARY KEY,
    trip_reference_id VARCHAR(255) UNIQUE         NOT NULL,
    provider_id       BIGINT REFERENCES providers (id),
    origin_code       VARCHAR(10)                 NOT NULL,
    destination_code  VARCHAR(10)                 NOT NULL,
    departure_time    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    arrival_time      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    vehicle_info      VARCHAR(255),
    trip_type         VARCHAR(50)                 NOT NULL, -- 'flight', 'train', 'bus'
    base_price        DECIMAL(10, 2)              NOT NULL,
    status            VARCHAR(50)                 NOT NULL, -- 'SCHEDULED', 'DELAYED', 'CANCELLED', 'COMPLETED'
    created_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Lưu trạng thái ghế dưới dạng bit string
CREATE TABLE trip_seat_status
(
    id              SERIAL PRIMARY KEY,
    trip_id         INTEGER REFERENCES trips (id) UNIQUE,

    vehicle_type    VARCHAR(50) NOT NULL,
    vehicle_model   VARCHAR(100),
    total_seats     INTEGER     NOT NULL,
    occupied_bitmap TEXT, -- Bit string: '1' = occupied, '0' = available 0000000
    blocked_bitmap  TEXT, -- Bit string cho ghế bị block
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Mapping ghế number với position trong bitmap
CREATE TABLE seat_position_map
(
    id               SERIAL PRIMARY KEY,
    vehicle_type     VARCHAR(50) NOT NULL,
    vehicle_model    VARCHAR(100),
    seat_number      VARCHAR(20) NOT NULL,
    bit_position     INTEGER     NOT NULL, -- Vị trí trong bitmap (0-based)
    seat_class       VARCHAR(50) NOT NULL,
    price_adjustment DECIMAL(10, 2) DEFAULT 0,
    UNIQUE (vehicle_type, vehicle_model, seat_number)
);

-- Index để tối ưu tìm kiếm chuyến đi theo địa điểm và thời gian
CREATE INDEX trips_origin_destination_idx ON trips (origin_code, destination_code);
CREATE INDEX trips_departure_time_idx ON trips (departure_time);