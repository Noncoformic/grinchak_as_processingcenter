--liquibase formatted sql
--changeset den:008-create-sales_point
CREATE TABLE sales_point (
                             id BIGSERIAL PRIMARY KEY,
                             pos_name VARCHAR(255) NOT NULL,
                             pos_address VARCHAR(255) NOT NULL,
                             pos_inn VARCHAR(12) NOT NULL UNIQUE,
                             acquiring_bank_id BIGINT NOT NULL
);

ALTER TABLE sales_point ADD CONSTRAINT fk_sales_point_acquiring_bank
    FOREIGN KEY (acquiring_bank_id) REFERENCES acquiring_bank(id);
