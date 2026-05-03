-- V1__init.sql

CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    phone         VARCHAR(20) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL,
    enabled       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Location master tables (predefined locations)
CREATE TABLE IF NOT EXISTS country (
    id   BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS state (
    id         BIGSERIAL PRIMARY KEY,
    country_id BIGINT NOT NULL REFERENCES country(id),
    code       VARCHAR(10) NOT NULL UNIQUE,
    name       VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS city (
    id       BIGSERIAL PRIMARY KEY,
    state_id BIGINT NOT NULL REFERENCES state(id),
    code     VARCHAR(20) NOT NULL UNIQUE,
    name     VARCHAR(120) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_city_state_id ON city(state_id);

-- Profile tables (will be used in Phase 2 part-B)
CREATE TABLE IF NOT EXISTS worker_profile (
    user_id           BIGINT PRIMARY KEY REFERENCES users(id),
    full_name         VARCHAR(120) NOT NULL,
    preferred_city_id BIGINT REFERENCES city(id),
    availability      VARCHAR(20),
    created_at        TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS employer_profile (
    user_id      BIGINT PRIMARY KEY REFERENCES users(id),
    company_name VARCHAR(150) NOT NULL,
    contact_name VARCHAR(120),
    city_id      BIGINT REFERENCES city(id),
    address_line VARCHAR(255),
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP
);