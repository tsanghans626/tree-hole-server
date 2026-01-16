CREATE TABLE personas
(
    id            BIGSERIAL PRIMARY KEY,
    code          VARCHAR(50) UNIQUE                  NOT NULL,
    name          VARCHAR(100)                        NOT NULL,
    description   VARCHAR(500),
    system_prompt TEXT,
    temperature   DOUBLE PRECISION,
    max_tokens    INTEGER,
    category      VARCHAR(50),
    version       VARCHAR(50),
    is_active     BOOLEAN   DEFAULT TRUE              NOT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_by     BIGINT    DEFAULT 0                 NOT NULL,
    update_by     BIGINT    DEFAULT 0                 NOT NULL
);

CREATE INDEX idx_personas_code ON personas (code);
CREATE INDEX idx_personas_is_active ON personas (is_active);
