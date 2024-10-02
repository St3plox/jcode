CREATE TABLE problems
(
    id          UUID PRIMARY KEY,
    user_id     BIGINT                                NOT NULL,
    title       VARCHAR(255)                        NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE test_cases
(
    id         UUID PRIMARY KEY,
    problem_id UUID                                NOT NULL,
    input      TEXT                                NOT NULL,
    output     TEXT                                NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_problem
        FOREIGN KEY (problem_id)
            REFERENCES problems (id)
            ON DELETE CASCADE
);

CREATE TABLE test_results
(
    id            UUID PRIMARY KEY,
    test_case_id  UUID                                NOT NULL,
    is_successful BOOLEAN                             NOT NULL,
    output        TEXT                                NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_test_case
        FOREIGN KEY (test_case_id)
            REFERENCES test_cases (id)
            ON DELETE CASCADE
);