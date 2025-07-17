--liquibase formatted sql
--changeset den:011-create-transaction_type
CREATE TABLE transaction_type (
                                  id BIGSERIAL PRIMARY KEY,
                                  transaction_type_name VARCHAR(255) NOT NULL UNIQUE,
                                  operator VARCHAR(1) NOT NULL
);
