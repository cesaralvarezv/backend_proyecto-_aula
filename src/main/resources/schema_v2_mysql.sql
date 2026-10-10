-- ============================================================================
-- YIREH STORE — ESQUEMA RELACIONAL COMPLETO (FASE 2)
-- Base de Datos: MySQL 8.x / MariaDB
-- Juego de Caracteres: utf8mb4_unicode_ci
-- Descripción: Estructura de tablas para catálogo, usuarios, billetera,
--              órdenes, auditoría, punto de venta (POS), arqueo de caja (Z-Report),
--              control de activos físicos y encuestas de satisfacción (CSAT).
-- ============================================================================

CREATE DATABASE IF NOT EXISTS yireh_store
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE yireh_store;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------------------------------------
-- 1. TABLA: users (Usuarios del sistema: Administradores, Cajeros, Estudiantes)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    balance DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 2. TABLA: user_roles (Roles normalizados: ADMIN, CASHIER, USER)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_name VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 3. TABLA: categories (Categorías de productos)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    icon VARCHAR(50),
    location VARCHAR(100),
    active TINYINT(1) NOT NULL DEFAULT 1,
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_categories_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 4. TABLA: products (Catálogo de productos e inventario de cafetería)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    category_id BIGINT,
    active TINYINT(1) NOT NULL DEFAULT 1,
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_products_category (category_id),
    INDEX idx_products_active (active),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 5. TABLA: carts (Carritos de compra de clientes)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(150) NOT NULL, -- Email del usuario asociado
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_carts_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 6. TABLA: cart_items (Ítems dentro del carrito)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(120) NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 7. TABLA: recharges (Historial de recargas de saldo de billetera)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS recharges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(150) NOT NULL, -- Email del usuario
    amount DECIMAL(12, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL, -- NEQUI, DAVIPLATA, CASH, CARD
    status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED', -- PENDING, COMPLETED, REJECTED
    reference_code VARCHAR(60) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_recharges_user (user_id),
    INDEX idx_recharges_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 8. TABLA: cash_closures (Arqueos y cierres de turno diario Z-Report)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cash_closures (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cashier_id BIGINT NOT NULL,
    cashier_email VARCHAR(150) NOT NULL,
    opening_amount DECIMAL(12, 2) NOT NULL, -- Base inicial en gaveta
    opening_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closing_time DATETIME NULL,
    cash_sales_system DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    virtual_wallet_sales DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    card_sales DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    total_sales_system DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    declared_cash DECIMAL(12, 2) NULL, -- Efectivo físico contado al cierre
    discrepancy DECIMAL(12, 2) NULL, -- Diferencia = declared_cash - (opening_amount + cash_sales_system)
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN', -- OPEN, CLOSED
    notes TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_closures_status (status),
    INDEX idx_closures_cashier (cashier_id),
    CONSTRAINT fk_closures_cashier FOREIGN KEY (cashier_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 9. TABLA: orders (Órdenes de compra — Móviles y Punto de Venta POS)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(30) NOT NULL UNIQUE,
    user_email VARCHAR(150) NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED
    payment_method VARCHAR(30) NOT NULL, -- WALLET, CASH, CARD, MIXED
    delivery_location VARCHAR(200),
    notes VARCHAR(500),
    order_type VARCHAR(20) NOT NULL DEFAULT 'MOBILE', -- MOBILE, POS_COUNTER
    cashier_id BIGINT NULL,
    closure_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_orders_user (user_email),
    INDEX idx_orders_status (status),
    INDEX idx_orders_created_at (created_at),
    INDEX idx_orders_closure (closure_id),
    CONSTRAINT fk_orders_closure FOREIGN KEY (closure_id) REFERENCES cash_closures(id) ON DELETE SET NULL,
    CONSTRAINT fk_orders_cashier FOREIGN KEY (cashier_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 10. TABLA: order_items (Líneas de detalle de cada pedido)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(120) NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 11. TABLA: physical_assets (Control e inventario de activos físicos y equipos)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS physical_assets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_code VARCHAR(50) NOT NULL UNIQUE, -- Placa física / Código QR / Código de barras
    name VARCHAR(150) NOT NULL, -- Ej: Cafetera Espresso 2 Grupos
    category VARCHAR(100) NOT NULL, -- Cafetería, Molinos, Congeladores, Terminales POS
    location VARCHAR(100) NULL, -- Barra Principal, Cocina, Bodega
    purchase_price DECIMAL(12, 2) NOT NULL,
    purchase_date DATE NOT NULL,
    useful_life_months INT NOT NULL DEFAULT 60,
    depreciation_accumulated DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    maintenance_schedule VARCHAR(255) NULL, -- Ej: Cada 3 meses - cambio de empaques y descalcificación
    last_maintenance_date DATE NULL,
    next_maintenance_date DATE NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OPERATIONAL', -- OPERATIONAL, IN_MAINTENANCE, DECOMMISSIONED
    notes TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_assets_code (asset_code),
    INDEX idx_assets_category (category),
    INDEX idx_assets_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 12. TABLA: satisfaction_surveys (Encuestas de satisfacción CSAT / NPS)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS satisfaction_surveys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    user_email VARCHAR(150) NOT NULL,
    rating INT NOT NULL, -- 1 a 5 estrellas
    quick_tags VARCHAR(500) NULL, -- Ej: Rápida entrega, Buena temperatura, Atención amable
    comment TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_surveys_order (order_id),
    INDEX idx_surveys_rating (rating),
    INDEX idx_surveys_created_at (created_at),
    CONSTRAINT fk_surveys_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- 13. TABLA: app_logs (Auditoría y registro centralizado de eventos del sistema)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS app_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    level VARCHAR(10) NOT NULL,
    source VARCHAR(20) NOT NULL,
    tag VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    stack_trace MEDIUMTEXT NULL,
    platform VARCHAR(50),
    device_info VARCHAR(255),
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_logs_level (level),
    INDEX idx_logs_source (source),
    INDEX idx_logs_timestamp (timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
