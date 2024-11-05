CREATE TABLE problem_results
(
    id            UUID PRIMARY KEY,
    problem_submission_id UUID NOT NULL,
    output        TEXT,
    errors        TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_submission
        FOREIGN KEY (problem_submission_id)
            REFERENCES problem_submissions (id)
            ON DELETE CASCADE
);