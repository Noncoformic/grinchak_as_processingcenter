--liquibase formatted sql
--changeset den:010-create-terminal
CREATE TABLE terminal (
                          id BIGSERIAL PRIMARY KEY,
                          terminal_id VARCHAR(9) NOT NULL UNIQUE,
                          mcc_id BIGINT NOT NULL,
                          pos_id BIGINT NOT NULL
);

ALTER TABLE terminal ADD CONSTRAINT fk_terminal_mcc
    FOREIGN KEY (mcc_id) REFERENCES merchant_category_code(id);

ALTER TABLE terminal ADD CONSTRAINT fk_terminal_sales_point
    FOREIGN KEY (pos_id) REFERENCES sales_point(id);
