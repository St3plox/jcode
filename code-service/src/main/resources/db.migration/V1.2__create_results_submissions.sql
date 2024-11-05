-- Migration script for Submission and Result tables

-- Create the 'submissions' table
CREATE TABLE submissions
(
    id                UUID PRIMARY KEY,
    user_id           BIGINT      NOT NULL,
    code              TEXT        NOT NULL,
    language          VARCHAR(50) NOT NULL,
    submission_status VARCHAR(50) NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the 'results' table
CREATE TABLE results
(
    id            UUID PRIMARY KEY,
    submission_id UUID NOT NULL,
    output        TEXT,
    errors        TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_submission
        FOREIGN KEY (submission_id)
            REFERENCES submissions (id)
            ON DELETE CASCADE
);

-- Indexes for faster lookup (optional)
CREATE INDEX idx_submission_user ON submissions (user_id);
CREATE INDEX idx_result_submission ON results (submission_id);
