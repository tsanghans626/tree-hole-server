CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    password VARCHAR(255),
    is_super_admin BOOLEAN DEFAULT FALSE NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    create_by BIGINT DEFAULT 0 NOT NULL,
    update_by BIGINT DEFAULT 0 NOT NULL,
    deleted BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_del_flag ON users(del_flag);
