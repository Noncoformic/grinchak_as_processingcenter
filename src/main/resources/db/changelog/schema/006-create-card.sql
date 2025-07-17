--liquibase formatted sql
--changeset den:006-create-card
CREATE TABLE card (
    id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(50) NOT NULL UNIQUE,
    holder_name VARCHAR(50) NOT NULL,
    expiration_date DATE NOT NULL,
    card_status_id BIGINT NOT NULL,
    payment_system_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    received_from_issuing_bank TIMESTAMP,
    sent_to_issuing_bank TIMESTAMP
);

ALTER TABLE card ADD CONSTRAINT fk_card_status
FOREIGN KEY (card_status_id) REFERENCES card_status(id);

ALTER TABLE card ADD CONSTRAINT fk_card_payment_system
FOREIGN KEY (payment_system_id) REFERENCES payment_system(id);

ALTER TABLE card ADD CONSTRAINT fk_card_account
FOREIGN KEY (account_id) REFERENCES account(id);
