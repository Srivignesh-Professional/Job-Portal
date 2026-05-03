-- V3__worker_multi_city.sql

-- Remove old single city column (only if exists)
ALTER TABLE worker_profile DROP COLUMN IF EXISTS preferred_city_id;

-- Junction table for multiple preferred cities
CREATE TABLE IF NOT EXISTS worker_preferred_city (
    worker_user_id BIGINT NOT NULL REFERENCES worker_profile(user_id),
    city_id        BIGINT NOT NULL REFERENCES city(id),
    PRIMARY KEY (worker_user_id, city_id)
);

CREATE INDEX IF NOT EXISTS idx_wpc_city_id ON worker_preferred_city(city_id);