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

-- -- Bảng lưu trữ thông tin hành trình khứ hồi
-- CREATE TABLE round_trips
-- (
--     id                      BIGSERIAL PRIMARY KEY,
--     round_trip_reference_id VARCHAR(255) UNIQUE          NOT NULL,                  -- Mã tham chiếu duy nhất cho hành trình khứ hồi
--     provider_id             BIGINT REFERENCES providers (id),
--
--     -- Thông tin chuyến đi
--     outbound_trip_id        BIGINT REFERENCES trips (id) NOT NULL,                  -- Chuyến đi
--     return_trip_id          BIGINT REFERENCES trips (id) NOT NULL,                  -- Chuyến về
--
--     -- Thông tin tổng hợp về hành trình
--     origin_code             VARCHAR(10)                  NOT NULL,                  -- Điểm xuất phát
--     destination_code        VARCHAR(10)                  NOT NULL,                  -- Điểm đến
--     outbound_departure      TIMESTAMP WITHOUT TIME ZONE  NOT NULL,                  -- Thời gian khởi hành chuyến đi
--     return_departure        TIMESTAMP WITHOUT TIME ZONE  NOT NULL,                  -- Thời gian khởi hành chuyến về
--
--     -- Thông tin giá cả và thanh toán
--     base_price              DECIMAL(10, 2)               NOT NULL,                  -- Giá cơ bản (có thể khác tổng 2 chuyến đơn lẻ)
--     discount_amount         DECIMAL(10, 2)                        DEFAULT 0,        -- Số tiền giảm giá cho khứ hồi
--     total_price             DECIMAL(10, 2)               NOT NULL,                  -- Tổng giá sau khi áp dụng khuyến mại
--     currency                VARCHAR(10)                           DEFAULT 'VND',
--
--     -- Thông tin trạng thái và quản lý
--     status                  VARCHAR(50)                  NOT NULL DEFAULT 'ACTIVE', -- 'ACTIVE', 'EXPIRED', 'CANCELLED', 'COMPLETED'
--     trip_type               VARCHAR(50)                  NOT NULL,                  -- 'flight', 'train', 'bus', 'mixed'
--
--     -- Thông tin về thời gian lưu trú
--     stay_duration_days      INTEGER                      NOT NULL,                  -- Số ngày lưu trú (calculated)
--
--     -- Thông tin bổ sung
--     booking_rules           JSONB,                                                  -- Quy tắc đặt vé (hủy, đổi, hoàn tiền)
--     package_benefits        JSONB,                                                  -- Lợi ích của gói khứ hồi
--     restrictions            JSONB,                                                  -- Các hạn chế (ngày blackout, seasonal)
--
--     -- Metadata
--     search_hash             VARCHAR(255),                                           -- Hash của tham số tìm kiếm để cache
--     valid_until             TIMESTAMP WITHOUT TIME ZONE,                            -- Thời hạn hiệu lực của giá
--     last_updated_from_api   TIMESTAMP WITHOUT TIME ZONE,                            -- Lần cuối cập nhật từ API nhà cung cấp
--
--     -- Audit fields
--     is_active               BOOLEAN                               DEFAULT TRUE,
--     is_deleted              BOOLEAN                               DEFAULT FALSE,
--     created_at              TIMESTAMP WITHOUT TIME ZONE           DEFAULT CURRENT_TIMESTAMP,
--     updated_at              TIMESTAMP WITHOUT TIME ZONE           DEFAULT CURRENT_TIMESTAMP,
--     created_by              VARCHAR(100),
--     updated_by              VARCHAR(100)
-- );

-- Bảng lưu trữ thông tin cấu hình cho chuyến đi khứ hồi
CREATE TABLE round_trip_configurations
(
    id                BIGSERIAL PRIMARY KEY,
    provider_id       BIGINT REFERENCES providers (id),
    min_days_stay     INTEGER,      -- Số ngày tối thiểu giữa chuyến đi và về
    max_days_stay     INTEGER,      -- Số ngày tối đa giữa chuyến đi và về
    origin_codes      VARCHAR(255),
    destination_codes VARCHAR(255),
    trip_type         VARCHAR(50),  -- Áp dụng cho loại chuyến đi: 'flight', 'train', 'bus', 'all'
    is_active         BOOLEAN                     DEFAULT TRUE,
    is_delete         BOOLEAN                     DEFAULT FALSE,
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