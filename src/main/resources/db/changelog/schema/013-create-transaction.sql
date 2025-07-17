--liquibase formatted sql
--changeset den:013-create-transaction
CREATE TABLE transaction (
    id BIGSERIAL PRIMARY KEY,
    transaction_date DATE NOT NULL,
    sum NUMERIC(19,2) NOT NULL,
    transaction_name VARCHAR(255) NOT NULL,
    account_id BIGINT NOT NULL,
    transaction_type_id BIGINT NOT NULL,
    card_id BIGINT,
    terminal_id BIGINT,
    response_code_id BIGINT,
    authorization_code VARCHAR(6),
    received_from_issuing_bank TIMESTAMP,
    sent_to_issuing_bank TIMESTAMP
);

ALTER TABLE transaction ADD CONSTRAINT fk_transaction_account
FOREIGN KEY (account_id) REFERENCES account(id);

ALTER TABLE transaction ADD CONSTRAINT fk_transaction_type
FOREIGN KEY (transaction_type_id) REFERENCES transaction_type(id);

ALTER TABLE transaction ADD CONSTRAINT fk_transaction_card
FOREIGN KEY (card_id) REFERENCES card(id);

ALTER TABLE transaction ADD CONSTRAINT fk_transaction_terminal
FOREIGN KEY (terminal_id) REFERENCES terminal(id);

ALTER TABLE transaction ADD CONSTRAINT fk_transaction_response_code
FOREIGN KEY (response_code_id) REFERENCES response_code(id);
