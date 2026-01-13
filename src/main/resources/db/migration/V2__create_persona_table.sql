CREATE TABLE personas (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    system_prompt TEXT,
    temperature DOUBLE PRECISION,
    max_tokens INTEGER,
    category VARCHAR(50),
    version VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_personas_code ON personas(code);
CREATE INDEX idx_personas_is_active ON personas(is_active);

-- Insert default persona
INSERT INTO personas (code, name, description, system_prompt, temperature, max_tokens, is_active)
VALUES ('default', 'Default Assistant', 'Default AI assistant persona',
        'You are a friendly and professional AI assistant named "Tree Hole". You are good at listening and providing warm support and advice.', 0.7, 2000, true);
