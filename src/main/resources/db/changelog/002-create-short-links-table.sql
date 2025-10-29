--liquibase formatted sql

--changeset alabuga-team:002-create-short-links-table
CREATE TABLE IF NOT EXISTS short_links (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    original_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    click_count BIGINT NOT NULL,
    last_accessed TIMESTAMP,
    user_id VARCHAR(64) NOT NULL
);

--changeset alabuga-team:002-create-short-links-table-constraints
ALTER TABLE short_links 
ADD CONSTRAINT fk_short_links_user_id 
FOREIGN KEY (user_id) REFERENCES users(id) 
ON DELETE CASCADE ON UPDATE CASCADE;

--changeset alabuga-team:002-create-short-links-table-indexes
CREATE INDEX IF NOT EXISTS idx_short_links_user_id ON short_links(user_id);
CREATE INDEX IF NOT EXISTS idx_short_links_created_at ON short_links(created_at);

--rollback DROP TABLE short_links;
