CREATE TABLE IF NOT EXISTS job_application (
    id            BIGSERIAL PRIMARY KEY,
    job_post_id   BIGINT NOT NULL REFERENCES job_post(id),
    worker_user_id BIGINT NOT NULL REFERENCES users(id),

    status        VARCHAR(20) NOT NULL,
    applied_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP,

    CONSTRAINT uq_job_worker UNIQUE(job_post_id, worker_user_id)
);

CREATE INDEX IF NOT EXISTS idx_job_application_worker ON job_application(worker_user_id);
CREATE INDEX IF NOT EXISTS idx_job_application_job ON job_application(job_post_id);