--liquibase formatted sql

--changeset alabuga-team:003-drop-fk-before-uuid
--comment: Drop FK and index before altering column types to UUID
ALTER TABLE short_links
DROP CONSTRAINT IF EXISTS fk_short_links_user_id;

DROP INDEX IF EXISTS idx_short_links_user_id;

--changeset alabuga-team:003-migrate-to-uuid-users
--comment: Миграция таблицы users: id из VARCHAR в UUID
ALTER TABLE users
ALTER COLUMN id TYPE UUID USING id::UUID;

--changeset alabuga-team:003-migrate-to-uuid-short-links
--comment: Миграция таблицы short_links: user_id из VARCHAR в UUID
ALTER TABLE short_links
ALTER COLUMN user_id TYPE UUID USING user_id::UUID;

--changeset alabuga-team:003-recreate-fk-after-uuid
--comment: Recreate FK and index after types are aligned to UUID
ALTER TABLE short_links
    ADD CONSTRAINT fk_short_links_user_id
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX IF NOT EXISTS idx_short_links_user_id ON short_links(user_id);

--changeset alabuga-team:003-add-user-fields
--comment: Добавление дополнительных полей пользователя
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS given_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS family_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS middle_name VARCHAR(100),
    ADD COLUMN IF NOT EXISTS full_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20),
    ADD COLUMN IF NOT EXISTS phone_number_verified BOOLEAN,
    ADD COLUMN IF NOT EXISTS email_verified BOOLEAN,
    ADD COLUMN IF NOT EXISTS gender VARCHAR(10);

--changeset alabuga-team:003-update-user-constraints
--comment: Обновление ограничений для новых полей
ALTER TABLE users
ALTER COLUMN username TYPE VARCHAR(50);

--changeset alabuga-team:003-add-last-accessed
--comment: Добавление поля last_accessed в таблицу short_links
ALTER TABLE short_links
    ADD COLUMN IF NOT EXISTS last_accessed TIMESTAMP;

--changeset alabuga-team:003-add-code-column
--comment: Добавление поля code в таблицу short_links
ALTER TABLE short_links
    ADD COLUMN IF NOT EXISTS code VARCHAR(20);

--changeset alabuga-team:003-populate-code-column
--comment: Заполнение поля code рандомными значениями для существующих записей
UPDATE short_links
SET code = 'temp' || id
WHERE code IS NULL;

--changeset alabuga-team:003-make-code-not-null
--comment: Делаем поле code NOT NULL и UNIQUE
ALTER TABLE short_links
    ALTER COLUMN code SET NOT NULL;

ALTER TABLE short_links
    ADD CONSTRAINT uk_short_links_code UNIQUE (code);

--rollback 
--rollback ALTER TABLE users ALTER COLUMN id TYPE VARCHAR(64);
--rollback ALTER TABLE short_links ALTER COLUMN user_id TYPE VARCHAR(64);
--rollback ALTER TABLE users DROP COLUMN IF EXISTS given_name, family_name, middle_name, full_name, phone_number, phone_number_verified, email_verified, gender;
