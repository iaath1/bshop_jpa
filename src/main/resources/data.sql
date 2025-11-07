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

INSERT INTO colors (name, hex_value) VALUES
('Белый',  '#FFFFFF'),
('Чёрный', '#000000'),
('Синий',  '#0000FF'),
('Красный', '#FF0000'),
('Зелёный', '#008000');

INSERT INTO roles(name) VALUES ('ROLE_CUSTOMER'), ('ROLE_ADMIN');

INSERT INTO statuses(name) VALUES ('NEW'), ('PAYED'), ('SHIPPING'), ('SHIPPED'), ('RETURNED');


INSERT INTO products (price, category_id, material_id, color_id) VALUES
(49.99, 1, 1, 1),
(59.99, 1, 2, 4),
(249.99, 2, 2, 2),
(279.99, 2, 2, 3),
(199.99, 3, 3, 3),
(219.99, 3, 3, 2),
(399.99, 4, 4, 2),
(189.99, 4, 5, 5),
(89.99, 5, 1, 5),
(99.99, 5, 2, 4);

-- 🇺🇦 УКРАИНСКИЕ ПЕРЕВОДЫ
INSERT INTO product_translations (product_id, language_code, name, description) VALUES
(1, 'uk', 'Футболка Basic White', 'Класична біла футболка з бавовни.'),
(2, 'uk', 'Футболка Sport Red', 'Спортивна футболка з дихаючої тканини.'),
(3, 'uk', 'Кросівки StreetRun', 'Зручні кросівки для міста та спорту.'),
(4, 'uk', 'Кросівки AirFlex', 'Легкі та амортизуючі кросівки.'),
(5, 'uk', 'Джинси Classic Blue', 'Прямі джинси з щільної тканини.'),
(6, 'uk', 'Джинси Slim Fit', 'Вузький фасон з еластичними волокнами.'),
(7, 'uk', 'Куртка Leather Black', 'Шкіряна куртка з підкладкою.'),
(8, 'uk', 'Куртка WindBreaker', 'Вітрівка з легкого нейлону.'),
(9, 'uk', 'Шорти Summer', 'Бавовняні шорти для спекотної погоди.'),
(10, 'uk', 'Шорти Sport Active', 'Спортивні шорти з сітчастою підкладкою.');

-- 🇵🇱 ПОЛЬСКИЕ ПЕРЕВОДЫ
INSERT INTO product_translations (product_id, language_code, name, description) VALUES
(1, 'pl', 'Koszulka Basic White', 'Klasyczna biała koszulka z bawełny.'),
(2, 'pl', 'Koszulka Sport Red', 'Sportowa koszulka z oddychającej tkaniny.'),
(3, 'pl', 'Buty StreetRun', 'Wygodne buty do miasta i sportu.'),
(4, 'pl', 'Buty AirFlex', 'Lekkie i amortyzujące buty sportowe.'),
(5, 'pl', 'Dżinsy Classic Blue', 'Proste dżinsy z gęstej tkaniny.'),
(6, 'pl', 'Dżinsy Slim Fit', 'Wąski krój z elastycznymi włóknami.'),
(7, 'pl', 'Kurtka Leather Black', 'Skórzana kurtka z podszewką.'),
(8, 'pl', 'Kurtka WindBreaker', 'Kurtka wiatrówka z lekkiego nylonu.'),
(9, 'pl', 'Szorty Summer', 'Bawełniane szorty na gorącą pogodę.'),
(10, 'pl', 'Szorty Sport Active', 'Sportowe szorty z siateczkową podszewką.');


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




