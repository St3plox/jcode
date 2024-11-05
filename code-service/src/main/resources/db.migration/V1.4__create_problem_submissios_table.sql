CREATE TABLE problem_submissions
(
    id                UUID PRIMARY KEY,
    user_id           BIGINT      NOT NULL,
    problem_id        UUID,
    code              TEXT        NOT NULL,
    language          VARCHAR(50) NOT NULL,
    submission_status VARCHAR(50) NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_problem FOREIGN KEY (problem_id) REFERENCES problems (id) ON DELETE CASCADE
);

