-- Изменение имени столбца в таблице users
ALTER TABLE users
    RENAME COLUMN update_at TO updated_at;

-- Изменение имени столбца в таблице goods
ALTER TABLE goods
    RENAME COLUMN update_at TO updated_at;

-- Изменение имени столбца в таблице payments
ALTER TABLE payments
    RENAME COLUMN update_at TO updated_at;
