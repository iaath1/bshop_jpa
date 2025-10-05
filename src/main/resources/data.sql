-- Категории
INSERT INTO categories (id, name) VALUES
(1, 'Бюстгальтеры'),
(2, 'Трусики'),
(3, 'Комплекты'),
(4, 'Аксессуары');

-- Размеры
INSERT INTO sizes (id, name) VALUES
(1, 'S'),
(2, 'M'),
(3, 'L'),
(4, 'XL');

-- Материалы
INSERT INTO materials (id, name) VALUES
(1, 'Хлопок'),
(2, 'Кружево'),
(3, 'Атлас'),
(4, 'Эластан');

-- Цвета
INSERT INTO colors (id, name) VALUES
(1, 'Черный'),
(2, 'Белый'),
(3, 'Красный'),
(4, 'Серый');

INSERT INTO roles(name) VALUES ('ROLE_CUSTOMER'), ('ROLE_ADMIN');

INSERT INTO statuses(name) VALUES ('NEW'), ('PAYED'), ('SHIPPING'), ('SHIPPED'), ('RETURNED');

INSERT INTO products (id, name, description, price, quantity, size_id, category_id, material_id, color_id) VALUES
(1, 'Бюстгальтер Push-up черный', 'Элегантный черный бюстгальтер с пуш-ап эффектом.', 2490, 15, 2, 1, 2, 1),
(2, 'Бюстгальтер кружевной белый', 'Классический белый бюстгальтер из кружева.', 2290, 20, 2, 1, 2, 2),
(3, 'Трусики кружевные красные', 'Соблазнительные кружевные трусики красного цвета.', 990, 30, 1, 2, 2, 3),
(4, 'Трусики хлопковые черные', 'Удобные хлопковые трусики на каждый день.', 790, 25, 3, 2, 1, 1),
(5, 'Комплект белья красный', 'Роскошный кружевной комплект (бюстгальтер + трусики).', 3990, 10, 2, 3, 2, 3),
(6, 'Комплект белья черный', 'Классический черный комплект из кружева.', 3790, 12, 3, 3, 2, 1),
(7, 'Комплект спортивный серый', 'Удобный спортивный комплект из хлопка.', 3190, 18, 3, 3, 1, 4),
(8, 'Чулки черные', 'Эластичные прозрачные чулки.', 690, 40, 2, 4, 4, 1),
(9, 'Пояс для чулок', 'Кружевной пояс для чулок.', 1190, 22, 2, 4, 2, 1),
(10, 'Ночной комплект сатиновый', 'Нежный комплект для сна из атласа.', 2890, 14, 3, 3, 3, 2);


ALTER SEQUENCE products_id_seq RESTART WITH 11;

INSERT INTO image (product_id, image_url) VALUES
-- Product 1
(1, '/uploads/products/1_1.png'),
(1, '/uploads/products/1_2.png'),
(1, '/uploads/products/1_3.png'),

-- Product 2
(2, '/uploads/products/2_1.png'),
(2, '/uploads/products/2_2.png'),

-- Product 3
(3, '/uploads/products/3_1.png'),
(3, '/uploads/products/3_2.png'),

-- Product 4
(4, '/uploads/products/4_1.png'),

-- Product 5
(5, '/uploads/products/5_1.png'),
(5, '/uploads/products/5_2.png'),
(5, '/uploads/products/5_3.png'),
(5, '/uploads/products/5_4.png'),

-- Product 6
(6, '/uploads/products/6_1.png'),
(6, '/uploads/products/6_2.png'),

-- Product 7
(7, '/uploads/products/7_1.png'),
(7, '/uploads/products/7_2.png'),

-- Product 8
(8, '/uploads/products/8_1.png'),
(8, '/uploads/products/8_2.png'),

-- Product 9
(9, '/uploads/products/9_1.png'),

-- Product 10
(10, '/uploads/products/10_1.png'),
(10, '/uploads/products/10_2.png');




