--liquibase formatted sql
--changeset arts:27_03_2026-1
CREATE TABLE IF NOT EXISTS restaurant (
    id VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    opening_hours VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    latitude DOUBLE PRECISION NOT NULL
);

--changeset edins:30_05_2026-3
ALTER TABLE restaurant
    ALTER COLUMN opening_hours TYPE JSONB
    USING opening_hours::JSONB;

--changeset edins:30_05_2026-2
INSERT INTO restaurant (
    id,
    created_at,
    updated_at,
    name,
    phone_number,
    opening_hours,
    address,
    city,
    country,
    longitude,
    latitude
) VALUES (
    '93e19a49-46a1-4238-a460-d3d75eefa100',
    NOW(),
    NOW(),
    'Glow Kitchen',
    '+45 12 34 56 78',
    '{"dailyOpeningHours":[{"dayOfWeek":"MONDAY","timeRanges":[{"open":"11:00:00","close":"22:00:00"}],"closed":false},{"dayOfWeek":"TUESDAY","timeRanges":[{"open":"11:00:00","close":"22:00:00"}],"closed":false},{"dayOfWeek":"WEDNESDAY","timeRanges":[{"open":"11:00:00","close":"22:00:00"}],"closed":false},{"dayOfWeek":"THURSDAY","timeRanges":[{"open":"11:00:00","close":"22:00:00"}],"closed":false},{"dayOfWeek":"FRIDAY","timeRanges":[{"open":"11:00:00","close":"23:00:00"}],"closed":false}]}',
    'Nørrebrogade 1',
    'Copenhagen',
    'Denmark',
    12.5683,
    55.6867
) ON CONFLICT (id) DO NOTHING;