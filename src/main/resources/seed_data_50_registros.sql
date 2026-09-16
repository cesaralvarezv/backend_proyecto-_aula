-- ============================================================================
-- SCRIPT DE INSERCIÓN DE DATOS DE PRUEBA (SEED DATA) — YIREH STORE
-- Total de registros: 61 registros relacionales consistentes
-- Compatible con: MySQL 8.x / MariaDB
-- Contraseña para todos los usuarios de prueba: 123456
-- Hash BCrypt: $2a$10$7Hvl5xwHE2yJbmlKmBPq.OuJuY1BlD55S6O9x9E23xD2XhXlaKoM.
-- ============================================================================

USE yireh_store;

SET FOREIGN_KEY_CHECKS = 0;

-- Limpieza opcional previa si se desea reiniciar datos de prueba
-- TRUNCATE TABLE cart_items;
-- TRUNCATE TABLE carts;
-- TRUNCATE TABLE order_items;
-- TRUNCATE TABLE orders;
-- TRUNCATE TABLE recharges;
-- TRUNCATE TABLE user_roles;
-- TRUNCATE TABLE users;
-- TRUNCATE TABLE products;
-- TRUNCATE TABLE categories;
-- TRUNCATE TABLE app_logs;

START TRANSACTION;

-- ----------------------------------------------------------------------------
-- 1. CATEGORÍAS (6 registros)
-- ----------------------------------------------------------------------------
INSERT INTO categories (id, name, description, icon, location, active, display_order, created_at, updated_at) VALUES
(1, 'Bebidas Calientes', 'Cafés especiales, infusiones, té y chocolates calientes', '☕', 'Primer Piso', 1, 1, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(2, 'Bebidas Frías & Jugos', 'Granizados, jugos naturales en agua o leche y gaseosas', '🥤', 'Primer Piso', 1, 2, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(3, 'Panadería & Repostería', 'Croissants recién horneados, pandebonos, tortas y galletas', '🥐', 'Piso 3', 1, 3, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(4, 'Desayunos & Combos', 'Combos estudiantiles con bebida y acompañamiento', '🍳', 'Primer Piso', 1, 4, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(5, 'Snacks & Paquetes', 'Papas artesanales, frutos secos, barras de cereal y dulces', '🍪', 'Piso 3', 1, 5, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(6, 'Almuerzos Rápidos', 'Sandwiches gourmet, wraps y ensaladas frescas', '🥪', 'Cafetería Central', 1, 6, '2026-09-01 08:00:00', '2026-09-01 08:00:00');

-- ----------------------------------------------------------------------------
-- 2. PRODUCTOS (12 registros)
-- ----------------------------------------------------------------------------
INSERT INTO products (id, name, description, price, quantity, image_url, category_id, active, display_order, created_at, updated_at) VALUES
(1, 'Café Americano 8oz', 'Café 100% arábica tostado medio con notas achocolatadas', 3500.00, 100, 'https://images.unsplash.com/photo-1509785307050-d4066910ec1e?w=400', 1, 1, 1, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(2, 'Capuchino Vainilla 12oz', 'Espresso doble con leche cremada y toque de jarabe de vainilla', 6500.00, 80, 'https://images.unsplash.com/photo-1534778101976-62847782c213?w=400', 1, 1, 2, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(3, 'Mocaccino Especial 12oz', 'Café espresso, chocolate artesanal oscuro y leche vaporizada', 7200.00, 60, 'https://images.unsplash.com/photo-1572442388796-11668a67e53d?w=400', 1, 1, 3, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(4, 'Granizado de Café Caramel', 'Bebida frappeada de café con crema chantilly y salsa de caramelo', 8500.00, 50, 'https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=400', 2, 1, 1, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(5, 'Jugo Natural de Maracuyá', 'Pulpa 100% natural preparada en agua o leche deslactosada', 5000.00, 70, 'https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?w=400', 2, 1, 2, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(6, 'Té Chai Frío con Leche', 'Infusión especiada con canela, cardamomo, clavo y leche de almendras', 6800.00, 45, 'https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=400', 2, 1, 3, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(7, 'Croissant de Mantequilla', 'Hojaldre francés crujiente con mantequilla pura importada', 4800.00, 90, 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=400', 3, 1, 1, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(8, 'Pandebono Tradicional', 'Receta típica con queso costeño, almidón de yuca y maíz', 3000.00, 120, 'https://images.unsplash.com/photo-1608198093002-ad4e005484ec?w=400', 3, 1, 2, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(9, 'Muffin de Arándanos', 'Esponjoso panecillo dulce relleno de arándanos frescos silvestres', 4500.00, 40, 'https://images.unsplash.com/photo-1586985289688-ca3cf47d3e6e?w=400', 3, 1, 3, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(10, 'Combo Empanada + Café Americano', 'Empanada de carne o pollo crocante acompañada de café 8oz', 5500.00, 85, 'https://images.unsplash.com/photo-1608039829572-78524f79c4c7?w=400', 4, 1, 1, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(11, 'Sandwich Gourmet Jamón y Queso', 'Pan ciabatta con jamón de pavo, queso mozzarella, lechuga y aderezo dijon', 9500.00, 35, 'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=400', 6, 1, 1, '2026-09-01 08:30:00', '2026-09-01 08:30:00'),
(12, 'Galleta de Avena, Miel y Chía', 'Snack saludable horneado sin azúcar refinada añadida', 3200.00, 60, 'https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=400', 5, 1, 1, '2026-09-01 08:30:00', '2026-09-01 08:30:00');

-- ----------------------------------------------------------------------------
-- 3. USUARIOS (5 registros)
-- Nota: Contraseña para todos es '123456'
-- ----------------------------------------------------------------------------
INSERT INTO users (id, name, phone, email, password_hash, balance, created_at) VALUES
(1, 'Administrador Yireh', '3001234567', 'admin@yirehstore.com', '$2a$10$7Hvl5xwHE2yJbmlKmBPq.OuJuY1BlD55S6O9x9E23xD2XhXlaKoM.', 250000.00, '2026-09-01 07:00:00'),
(2, 'Carlos Pérez Restrepo', '3109876543', 'carlos.perez@yirehstore.com', '$2a$10$7Hvl5xwHE2yJbmlKmBPq.OuJuY1BlD55S6O9x9E23xD2XhXlaKoM.', 45000.00, '2026-09-02 09:15:00'),
(3, 'María Gómez Londoño', '3156781234', 'maria.gomez@yirehstore.com', '$2a$10$7Hvl5xwHE2yJbmlKmBPq.OuJuY1BlD55S6O9x9E23xD2XhXlaKoM.', 32000.00, '2026-09-03 10:30:00'),
(4, 'Juan David Rodríguez', '3204567890', 'juan.rodriguez@yirehstore.com', '$2a$10$7Hvl5xwHE2yJbmlKmBPq.OuJuY1BlD55S6O9x9E23xD2XhXlaKoM.', 18500.00, '2026-09-04 14:20:00'),
(5, 'Laura Martínez Castro', '3017894561', 'laura.martinez@yirehstore.com', '$2a$10$7Hvl5xwHE2yJbmlKmBPq.OuJuY1BlD55S6O9x9E23xD2XhXlaKoM.', 60000.00, '2026-09-05 11:45:00');

-- ----------------------------------------------------------------------------
-- 4. ROLES DE USUARIOS (6 registros)
-- ----------------------------------------------------------------------------
INSERT INTO user_roles (user_id, role_name) VALUES
(1, 'ADMIN'),
(1, 'USER'),
(2, 'USER'),
(3, 'USER'),
(4, 'USER'),
(5, 'USER');

-- ----------------------------------------------------------------------------
-- 5. CARRITOS ACTIVOS (4 registros)
-- ----------------------------------------------------------------------------
INSERT INTO carts (id, user_id, created_at, updated_at) VALUES
(1, 'carlos.perez@yirehstore.com', '2026-09-02 09:20:00', '2026-09-12 08:30:00'),
(2, 'maria.gomez@yirehstore.com', '2026-09-03 10:35:00', '2026-09-12 09:00:00'),
(3, 'juan.rodriguez@yirehstore.com', '2026-09-04 14:25:00', '2026-09-12 09:15:00'),
(4, 'laura.martinez@yirehstore.com', '2026-09-05 11:50:00', '2026-09-12 09:20:00');

-- ----------------------------------------------------------------------------
-- 6. ÍTEMS EN CARRITO (5 registros)
-- ----------------------------------------------------------------------------
INSERT INTO cart_items (id, product_id, product_name, unit_price, quantity, cart_id) VALUES
(1, 2, 'Capuchino Vainilla 12oz', 6500.00, 2, 1),
(2, 7, 'Croissant de Mantequilla', 4800.00, 1, 1),
(3, 4, 'Granizado de Café Caramel', 8500.00, 1, 2),
(4, 11, 'Sandwich Gourmet Jamón y Queso', 9500.00, 1, 3),
(5, 8, 'Pandebono Tradicional', 3000.00, 3, 4);

-- ----------------------------------------------------------------------------
-- 7. RECARGAS DE BILLETERA (6 registros)
-- ----------------------------------------------------------------------------
INSERT INTO recharges (id, user_id, amount, payment_method, status, reference_code, created_at) VALUES
(1, 'carlos.perez@yirehstore.com', 50000.00, 'NEQUI', 'COMPLETED', 'REC-20260910-101', '2026-09-10 08:15:00'),
(2, 'carlos.perez@yirehstore.com', 20000.00, 'CASH', 'COMPLETED', 'REC-20260911-204', '2026-09-11 10:00:00'),
(3, 'maria.gomez@yirehstore.com', 40000.00, 'DAVIPLATA', 'COMPLETED', 'REC-20260910-305', '2026-09-10 14:30:00'),
(4, 'juan.rodriguez@yirehstore.com', 25000.00, 'NEQUI', 'COMPLETED', 'REC-20260911-409', '2026-09-11 09:45:00'),
(5, 'laura.martinez@yirehstore.com', 70000.00, 'NEQUI', 'COMPLETED', 'REC-20260911-512', '2026-09-11 16:20:00'),
(6, 'laura.martinez@yirehstore.com', 15000.00, 'CASH', 'COMPLETED', 'REC-20260912-615', '2026-09-12 08:10:00');

-- ----------------------------------------------------------------------------
-- 8. PEDIDOS / ÓRDENES (5 registros)
-- Estados: PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED
-- ----------------------------------------------------------------------------
INSERT INTO orders (id, order_number, user_email, total, status, payment_method, delivery_location, notes, created_at, updated_at) VALUES
(1, 'PED-20260910-1001', 'carlos.perez@yirehstore.com', 11300.00, 'DELIVERED', 'WALLET', 'Piso 1 - Aula 102', 'Entregar caliente por favor', '2026-09-10 09:00:00', '2026-09-10 09:25:00'),
(2, 'PED-20260911-1002', 'maria.gomez@yirehstore.com', 8500.00, 'DELIVERED', 'WALLET', 'Piso 3 - Sala de Profesores', 'Sin pitillo plástico', '2026-09-11 11:15:00', '2026-09-11 11:40:00'),
(3, 'PED-20260912-1003', 'juan.rodriguez@yirehstore.com', 9500.00, 'READY', 'CASH', 'Cafetería Central', 'Llevar cambio de 20.000', '2026-09-12 08:45:00', '2026-09-12 09:05:00'),
(4, 'PED-20260912-1004', 'laura.martinez@yirehstore.com', 14300.00, 'PREPARING', 'WALLET', 'Piso 2 - Laboratorio 4', 'Bebida sin azúcar', '2026-09-12 09:10:00', '2026-09-12 09:15:00'),
(5, 'PED-20260912-1005', 'carlos.perez@yirehstore.com', 5500.00, 'CONFIRMED', 'NEQUI', 'Piso 3 - Terraza', 'Llevar servilletas extra', '2026-09-12 09:20:00', '2026-09-12 09:22:00');

-- ----------------------------------------------------------------------------
-- 9. DETALLE DE ÍTEMS DE PEDIDOS (8 registros)
-- ----------------------------------------------------------------------------
INSERT INTO order_items (id, order_id, product_id, product_name, unit_price, quantity) VALUES
(1, 1, 2, 'Capuchino Vainilla 12oz', 6500.00, 1),
(2, 1, 7, 'Croissant de Mantequilla', 4800.00, 1),
(3, 2, 4, 'Granizado de Café Caramel', 8500.00, 1),
(4, 3, 11, 'Sandwich Gourmet Jamón y Queso', 9500.00, 1),
(5, 4, 3, 'Mocaccino Especial 12oz', 7200.00, 1),
(6, 4, 9, 'Muffin de Arándanos', 4500.00, 1),
(7, 4, 12, 'Galleta de Avena, Miel y Chía', 3200.00, 1),
(8, 5, 10, 'Combo Empanada + Café Americano', 5500.00, 1);

-- ----------------------------------------------------------------------------
-- 10. REGISTROS DE AUDITORÍA / LOGS (4 registros)
-- ----------------------------------------------------------------------------
INSERT INTO app_logs (id, level, source, tag, message, stack_trace, platform, device_info, timestamp) VALUES
(1, 'INFO', 'BACKEND', 'DataInitializer', 'Inicialización del sistema Yireh Store completada con éxito.', NULL, 'server', 'Spring Boot 3.5 Embedded Tomcat', '2026-09-12 07:00:00'),
(2, 'INFO', 'BACKEND', 'WalletService', 'Recarga procesada exitosamente para usuario: carlos.perez@yirehstore.com por $50000.00 COP. Ref: REC-20260910-101', NULL, 'server', 'PostgreSQL / HikariCP Pool', '2026-09-10 08:15:00'),
(3, 'INFO', 'FRONTEND', 'CartController', 'Sincronización de carrito local-first ejecutada correctamente en dispositivo.', NULL, 'android', 'Pixel 7 Pro - Android 14 API 34', '2026-09-12 08:30:15'),
(4, 'INFO', 'BACKEND', 'OrderService', 'Orden PED-20260912-1004 creada y enviada a cola de preparación de cocina.', NULL, 'server', 'Spring Boot Orders Dispatcher', '2026-09-12 09:10:00');

COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- VERIFICACIÓN DE CONTEO DE REGISTROS INSERTADOS:
-- ============================================================================
SELECT 'categories' AS tabla, COUNT(*) AS total FROM categories
UNION ALL
SELECT 'products', COUNT(*) FROM products
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'user_roles', COUNT(*) FROM user_roles
UNION ALL
SELECT 'carts', COUNT(*) FROM carts
UNION ALL
SELECT 'cart_items', COUNT(*) FROM cart_items
UNION ALL
SELECT 'recharges', COUNT(*) FROM recharges
UNION ALL
SELECT 'orders', COUNT(*) FROM orders
UNION ALL
SELECT 'order_items', COUNT(*) FROM order_items
UNION ALL
SELECT 'app_logs', COUNT(*) FROM app_logs;
