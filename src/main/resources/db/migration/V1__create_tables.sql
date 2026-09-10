CREATE TABLE routine (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    scheduled_at TIMESTAMP,
    status VARCHAR(20),
    completed_at TIMESTAMP,
    created_at TIMESTAMP,
    phone_number VARCHAR(50)
);

CREATE TABLE idea (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    phone_number VARCHAR(50)
);

CREATE TABLE reminder (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255),
    trigger_at TIMESTAMP,
    phone_number VARCHAR(50),
    fired BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP
);