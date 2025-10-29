-- Миграция для преобразования типов колонок из VARCHAR в UUID
-- Выполните этот скрипт в вашей базе данных PostgreSQL

-- 1. Преобразование таблицы users: id из VARCHAR в UUID
ALTER TABLE users 
ALTER COLUMN id TYPE uuid USING id::uuid;

-- 2. Преобразование таблицы short_links: user_id из VARCHAR в UUID
ALTER TABLE short_links 
ALTER COLUMN user_id TYPE uuid USING user_id::uuid;

-- Проверка результата
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name IN ('users', 'short_links') 
AND column_name IN ('id', 'user_id');

