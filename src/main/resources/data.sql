INSERT INTO categories (name) VALUES
('Футболки'),
('Кроссовки'),
('Джинсы'),
('Куртки'),
('Шорты');

INSERT INTO materials (name) VALUES
('Хлопок'),
('Полиэстер'),
('Джинса'),
('Кожа'),
('Нейлон');

INSERT INTO colors (name) VALUES
('Белый'),
('Черный'),
('Синий'),
('Красный'),
('Зелёный');

INSERT INTO roles(name) VALUES ('ROLE_CUSTOMER'), ('ROLE_ADMIN');

INSERT INTO statuses(name) VALUES ('NEW'), ('PAYED'), ('SHIPPING'), ('SHIPPED'), ('RETURNED');


INSERT INTO products (name, description, price, category_id, material_id, color_id) VALUES
('Футболка Basic White', 'Классическая белая футболка из хлопка.', 49.99, 1, 1, 1),
('Футболка Sport Red', 'Спортивная футболка с дышащей тканью.', 59.99, 1, 2, 4),
('Кроссовки StreetRun', 'Удобные кроссовки для города и спорта.', 249.99, 2, 2, 2),
('Кроссовки AirFlex', 'Лёгкие и амортизирующие кроссовки.', 279.99, 2, 2, 3),
('Джинсы Classic Blue', 'Прямые джинсы из плотной ткани.', 199.99, 3, 3, 3),
('Джинсы Slim Fit', 'Узкий фасон с эластичными волокнами.', 219.99, 3, 3, 2),
('Куртка Leather Black', 'Кожаная куртка с подкладкой.', 399.99, 4, 4, 2),
('Куртка WindBreaker', 'Ветровка из лёгкого нейлона.', 189.99, 4, 5, 5),
('Шорты Summer', 'Хлопковые шорты для жаркой погоды.', 89.99, 5, 1, 5),
('Шорты Sport Active', 'Спортивные шорты с сетчатой подкладкой.', 99.99, 5, 2, 4);

-- Футболка Basic White
INSERT INTO sizes (name, quantity, product_id) VALUES
('S', 15, 1),
('M', 30, 1),
('L', 20, 1),
('XL', 0, 1),
('XS', 0 ,1);

-- Футболка Sport Red
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 2),
('S', 0, 2),
('M', 25, 2),
('L', 35, 2),
('XL', 10, 2);

-- Кроссовки StreetRun
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 3),
('S', 0, 3),
('M', 20, 3),
('L', 25, 3),
('XL', 15, 3);

-- Кроссовки AirFlex
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 10, 4),
('S', 20, 4),
('M', 15, 4),
('L', 10, 4),
('XL', 0, 4);

-- Джинсы Classic Blue
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 5),
('S', 15, 5),
('M', 25, 5),
('L', 10, 5),
('XL', 0, 5);

-- Джинсы Slim Fit
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 6),
('S', 0, 6),
('M', 20, 6),
('L', 25, 6),
('XL', 10, 6);

-- Куртка Leather Black
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 7),
('S', 0, 7),
('M', 10, 7),
('L', 15, 7),
('XL', 5, 7);

-- Куртка WindBreaker
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 8),
('S', 15, 8),
('M', 20, 8),
('L', 10, 8),
('XL', 0, 8);

-- Шорты Summer
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 9),
('S', 10, 9),
('M', 20, 9),
('L', 15, 9),
('XL', 0, 9);

-- Шорты Sport Active
INSERT INTO sizes (name, quantity, product_id) VALUES
('XS', 0, 10),
('S', 0, 10),
('M', 15, 10),
('L', 25, 10),
('XL', 10, 10);




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




