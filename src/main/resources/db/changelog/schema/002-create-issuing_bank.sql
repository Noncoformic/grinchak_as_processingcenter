--liquibase formatted sql
--changeset den:002-create-issuing-bank
CREATE TABLE issuing_bank (
    id BIGSERIAL PRIMARY KEY,
    bic VARCHAR(9) NOT NULL UNIQUE,
    bin VARCHAR(5),
    abbreviated_name VARCHAR(255) NOT NULL
);
