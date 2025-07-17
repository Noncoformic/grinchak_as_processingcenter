--liquibase formatted sql
--changeset den:007-create-acquiring_bank
CREATE TABLE acquiring_bank (
    id BIGSERIAL PRIMARY KEY,
    bic VARCHAR(9) NOT NULL UNIQUE,
    abbreviated_name VARCHAR(255) NOT NULL
);
