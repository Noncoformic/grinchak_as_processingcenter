--liquibase formatted sql
--changeset den:101-refdata context:prod,dev

-- Валюты
INSERT INTO currency (id, currency_digital_code, currency_letter_code, currency_name)
VALUES
    (1, '643', 'RUB', 'Российский рубль'),
    (2, '840', 'USD', 'Доллар США');

-- Статусы карт
INSERT INTO card_status (id, card_status_name)
VALUES
    (1, 'ACTIVE'),
    (2, 'BLOCKED');

-- Платежные системы
INSERT INTO payment_system (id, payment_system_name)
VALUES
    (1, 'VISA'),
    (2, 'MASTERCARD');

-- Типы транзакций
INSERT INTO transaction_type (id, transaction_type_name, operator)
VALUES
    (1, 'Purchase', '-'),
    (2, 'Refund', '+');

-- Коды ответов
INSERT INTO response_code (id, error_code, error_description, error_level)
VALUES
    (1, '0', 'Approved', 'INFO'),
    (2, '5', 'Do not honor', 'ERROR');

-- MCC коды
INSERT INTO merchant_category_code (id, mcc, mcc_name)
VALUES
    (1, '5411', 'Grocery Stores'),
    (2, '5812', 'Eating Places, Restaurants');
