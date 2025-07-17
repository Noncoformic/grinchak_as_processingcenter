--liquibase formatted sql
--changeset den:004-create-card-status
CREATE TABLE card_status (
                             id BIGSERIAL PRIMARY KEY,
                             card_status_name VARCHAR(255) NOT NULL UNIQUE
);
