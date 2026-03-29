-- =============================================
-- V1: Initial schema
-- =============================================

CREATE TABLE IF NOT EXISTS categories (
    id      BIGSERIAL PRIMARY KEY,
    name_ua VARCHAR(30) NOT NULL UNIQUE,
    name_pl VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS materials (
    id      BIGSERIAL PRIMARY KEY,
    name_ua VARCHAR(20) NOT NULL UNIQUE,
    name_pl VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS colors (
    id        BIGSERIAL PRIMARY KEY,
    name_ua   VARCHAR(20) NOT NULL UNIQUE,
    name_pl   VARCHAR(20) NOT NULL UNIQUE,
    hex_value VARCHAR(7)
);

CREATE TABLE IF NOT EXISTS roles (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS statuses (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS shop_users (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255),
    surname    VARCHAR(255),
    email      VARCHAR(100) NOT NULL UNIQUE,
    phone      VARCHAR(15)  UNIQUE,
    password   VARCHAR(100) NOT NULL,
    is_active  BOOLEAN      NOT NULL DEFAULT FALSE,
    address    VARCHAR(255),
    created_at TIMESTAMP    DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES shop_users (id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles (id)      ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS products (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100),
    description VARCHAR(200),
    price       DECIMAL(10, 2) NOT NULL,
    quantity    INTEGER,
    category_id BIGINT REFERENCES categories (id),
    material_id BIGINT REFERENCES materials  (id),
    color_id    BIGINT REFERENCES colors     (id)
);

CREATE TABLE IF NOT EXISTS product_translations (
    id            BIGSERIAL PRIMARY KEY,
    product_id    BIGINT       NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    language_code VARCHAR(10),
    name          VARCHAR(255),
    description   TEXT
);

CREATE TABLE IF NOT EXISTS sizes (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(4)  NOT NULL,
    quantity   INTEGER     NOT NULL,
    product_id BIGINT      NOT NULL REFERENCES products (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS image (
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT       NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    image_url  VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS carts (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE REFERENCES shop_users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cart_items (
    id         BIGSERIAL PRIMARY KEY,
    cart_id    BIGINT  NOT NULL REFERENCES carts    (id) ON DELETE CASCADE,
    product_id BIGINT  NOT NULL REFERENCES products (id),
    size_id    BIGINT  NOT NULL REFERENCES sizes    (id),
    quantity   INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT         REFERENCES shop_users (id),
    status_id       BIGINT         NOT NULL REFERENCES statuses (id),
    created_at      TIMESTAMP      DEFAULT NOW(),
    updated_at      TIMESTAMP,
    total_price     DECIMAL(10, 2) NOT NULL,
    total_price_gr  BIGINT         NOT NULL,
    guest_email     VARCHAR(255),
    locker_id       VARCHAR(255),
    locker_name     VARCHAR(255),
    locker_address  VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS order_items (
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT         NOT NULL REFERENCES orders   (id) ON DELETE CASCADE,
    product_id BIGINT         NOT NULL REFERENCES products (id),
    size_id    BIGINT         NOT NULL REFERENCES sizes    (id),
    quantity   INTEGER        NOT NULL,
    price      DECIMAL(10, 2) NOT NULL
);
