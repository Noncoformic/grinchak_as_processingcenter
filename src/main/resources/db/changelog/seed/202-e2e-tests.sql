--liquibase formatted sql
--changeset den:202-e2e-tests context:e2e

-- Учетная запись для e2e
INSERT INTO account (id, account_number, balance, currency_id, issuing_bank_id)
VALUES (2, 'TEST00001', 5000.00, 1, 1);

-- Карта для e2e
INSERT INTO card (id, card_number, holder_name, expiration_date, card_status_id, payment_system_id, account_id)
VALUES (2, '4000 5678 9012 0002', 'Test User', '2026-12-31', 1, 2, 2);

-- Терминал для e2e
INSERT INTO terminal (id, terminal_id, mcc_id, pos_id)
VALUES (2, 'TERM002', 2, 1);

-- Транзакция для e2e
INSERT INTO transaction (id, transaction_date, sum, transaction_name, account_id, transaction_type_id, card_id, terminal_id, response_code_id, authorization_code)
VALUES (1, CURRENT_DATE, 100.00, 'Test Purchase', 2, 1, 2, 2, 1, 'ABC123');
