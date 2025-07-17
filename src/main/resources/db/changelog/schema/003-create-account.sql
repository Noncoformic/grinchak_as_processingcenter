--liquibase formatted sql
--changeset den:003-create-account
CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance NUMERIC(19,2) NOT NULL,
    currency_id BIGINT NOT NULL,
    issuing_bank_id BIGINT NOT NULL
);

ALTER TABLE account ADD CONSTRAINT fk_account_currency
FOREIGN KEY (currency_id) REFERENCES currency(id);

ALTER TABLE account ADD CONSTRAINT fk_account_issuing_bank
FOREIGN KEY (issuing_bank_id) REFERENCES issuing_bank(id);
