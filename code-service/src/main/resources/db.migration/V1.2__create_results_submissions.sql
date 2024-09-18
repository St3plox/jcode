-- Create table for 'Submission' entity
CREATE TABLE submissions
(
    id                UUID        NOT NULL PRIMARY KEY,
    user_id           BIGINT      NOT NULL,
    code              TEXT,
    language          VARCHAR(20) NOT NULL,
    submission_status VARCHAR(20) NOT NULL,
    created_at        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create table for 'Result' entity
CREATE TABLE results
(
    id            UUID      NOT NULL PRIMARY KEY,
    submission_id UUID      NOT NULL,
    output        TEXT,
    errors        TEXT,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_submission FOREIGN KEY (submission_id) REFERENCES submissions (id) ON DELETE CASCADE
);

CREATE INDEX idx_submission_user ON submissions (user_id);
CREATE INDEX idx_result_submission ON results (submission_id);
