--liquibase formatted sql

--changeset alabuga-team:001-create-users-table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(64) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL
);

--rollback DROP TABLE users;
