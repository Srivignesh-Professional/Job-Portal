CREATE TABLE IF NOT EXISTS job_category (
    id      BIGSERIAL PRIMARY KEY,
    code    VARCHAR(30) NOT NULL UNIQUE,
    name    VARCHAR(120) NOT NULL,
    active  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS job_post (
    id               BIGSERIAL PRIMARY KEY,
    employer_user_id BIGINT NOT NULL REFERENCES users(id),
    category_id      BIGINT NOT NULL REFERENCES job_category(id),
    city_id          BIGINT NOT NULL REFERENCES city(id),

    title            VARCHAR(160) NOT NULL,
    description      TEXT,

    wage_type        VARCHAR(20) NOT NULL,
    wage_amount      NUMERIC(10,2) NOT NULL,

    work_date        DATE,
    status           VARCHAR(20) NOT NULL,

    created_at       TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_job_post_employer ON job_post(employer_user_id);
CREATE INDEX IF NOT EXISTS idx_job_post_city ON job_post(city_id);
CREATE INDEX IF NOT EXISTS idx_job_post_category ON job_post(category_id);
CREATE INDEX IF NOT EXISTS idx_job_post_status ON job_post(status);