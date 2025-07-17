--liquibase formatted sql
--changeset den:001-create-currency
CREATE TABLE currency (
    id BIGSERIAL PRIMARY KEY,
    currency_digital_code VARCHAR(3) NOT NULL UNIQUE,
    currency_letter_code VARCHAR(3) NOT NULL UNIQUE,
    currency_name VARCHAR(255) NOT NULL
);
