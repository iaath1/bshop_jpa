-- V3__add_product_timestamps.sql
ALTER TABLE products
    ADD COLUMN IF NOT EXISTS created_at  TIMESTAMP DEFAULT NOW(),
    ADD COLUMN IF NOT EXISTS updated_at  TIMESTAMP;