--liquibase formatted sql
--changeset den:201-dev-demo context:dev

-- Банк-эмитент
INSERT INTO issuing_bank (id, bic, bin, abbreviated_name)
VALUES (1, '044525225', '12345', 'Учебный Банк');

-- Учетная запись
INSERT INTO account (id, account_number, balance, currency_id, issuing_bank_id)
VALUES (1, 'DEV00001', 10000.00, 1, 1);

-- Карта
INSERT INTO card (id, card_number, holder_name, expiration_date, card_status_id, payment_system_id, account_id)
VALUES (1, '4000 1234 5678 0001', 'Ivan Ivanov', '2026-12-31', 1, 1, 1);

-- Банк-эквайер
INSERT INTO acquiring_bank (id, bic, abbreviated_name)
VALUES (1, '044525226', 'Учебный Эквайер');

-- Торговая точка
INSERT INTO sales_point (id, pos_name, pos_address, pos_inn, acquiring_bank_id)
VALUES (1, 'DEMO POS', 'Moscow, Tverskaya 1', '7701234567', 1);

-- Терминал
INSERT INTO terminal (id, terminal_id, mcc_id, pos_id)
VALUES (1, 'TERM001', 1, 1);
