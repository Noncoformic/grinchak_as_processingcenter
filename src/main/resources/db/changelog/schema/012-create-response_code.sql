--liquibase formatted sql
--changeset den:012-create-response_code
CREATE TABLE response_code (
    id BIGSERIAL PRIMARY KEY,
    error_code VARCHAR(10) NOT NULL UNIQUE,
    error_description VARCHAR(255),
    error_level VARCHAR(50)
);
