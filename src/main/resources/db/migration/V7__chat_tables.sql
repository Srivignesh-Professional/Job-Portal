CREATE TABLE IF NOT EXISTS chat_session (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS chat_message (
    id         BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES chat_session(id),
    sender     VARCHAR(10) NOT NULL, -- USER / BOT
    message    TEXT NOT NULL,
    sent_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_chat_session_user_id ON chat_session(user_id);
CREATE INDEX IF NOT EXISTS idx_chat_message_session_id ON chat_message(session_id);