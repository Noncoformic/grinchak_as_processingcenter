--liquibase formatted sql
--changeset den:009-create-merchant_category_code
CREATE TABLE merchant_category_code (
                                        id BIGSERIAL PRIMARY KEY,
                                        mcc VARCHAR(4) NOT NULL UNIQUE,
                                        mcc_name VARCHAR(255) NOT NULL
);
