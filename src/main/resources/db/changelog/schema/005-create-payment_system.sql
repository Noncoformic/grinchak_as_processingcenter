--liquibase formatted sql
--changeset den:005-create-payment-system
CREATE TABLE payment_system (
    id BIGSERIAL PRIMARY KEY,
    payment_system_name VARCHAR(50) NOT NULL UNIQUE
);
