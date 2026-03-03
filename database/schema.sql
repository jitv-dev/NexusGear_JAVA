-- ============================================================
-- NEXUS GEAR — Base de Datos MySQL
-- Proyecto Módulo 5 — Java EE / MVC / DAO / JDBC
-- ============================================================

CREATE DATABASE IF NOT EXISTS nexusgear_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE nexusgear_db;

-- ===== USUARIOS =====
CREATE TABLE usuarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    apellido    VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,        -- SHA-256
    rol         ENUM('ADMIN','CLIENTE') DEFAULT 'CLIENTE',
    activo      BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== CATEGORIAS =====
CREATE TABLE categorias (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    slug    VARCHAR(100) NOT NULL UNIQUE,
    icono   VARCHAR(20)
);

-- ===== MARCAS =====
CREATE TABLE marcas (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL UNIQUE
);

-- ===== PRODUCTOS =====
CREATE TABLE productos (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(200) NOT NULL,
    descripcion     TEXT,
    especificaciones VARCHAR(300),
    precio          DECIMAL(12,2) NOT NULL,
    precio_anterior DECIMAL(12,2),
    stock           INT DEFAULT 0,
    imagen          VARCHAR(10),              -- emoji como imagen
    categoria_id    INT,
    marca_id        INT,
    badge           VARCHAR(10),             -- 'new', 'hot', 'sale'
    activo          BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    FOREIGN KEY (marca_id)     REFERENCES marcas(id)
);

-- ===== ORDENES =====
CREATE TABLE ordenes (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    numero_orden    VARCHAR(30) NOT NULL UNIQUE,
    usuario_id      INT NOT NULL,
    total           DECIMAL(12,2) NOT NULL,
    estado          ENUM('PENDIENTE','PAGADO','ENVIADO','ENTREGADO','CANCELADO') DEFAULT 'PENDIENTE',
    direccion       VARCHAR(300),
    ciudad          VARCHAR(100),
    telefono        VARCHAR(30),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ===== DETALLE ORDEN =====
CREATE TABLE orden_items (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    orden_id    INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad    INT NOT NULL,
    precio_unit DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (orden_id)    REFERENCES ordenes(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- ============================================================
-- DATOS INICIALES
-- ============================================================

-- Usuarios (password: "123456" → SHA-256)
INSERT INTO usuarios (nombre, apellido, email, password, rol) VALUES
('Admin',   'NexusGear', 'admin@nexusgear.cl',  '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ADMIN'),
('Juan',    'Pérez',     'juan@gmail.com',       '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'CLIENTE'),
('María',   'González',  'maria@gmail.com',      '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'CLIENTE'),
('Carlos',  'López',     'carlos@gmail.com',     '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'CLIENTE');

-- Categorías
INSERT INTO categorias (nombre, slug, icono) VALUES
('Teclados',      'teclados',     '⌨️'),
('Mouses',        'mouses',       '🖱️'),
('Headsets',      'headsets',     '🎧'),
('Monitores',     'monitores',    '🖥️'),
('Sillas Gamer',  'sillas',       '🪑'),
('Webcams',       'webcams',      '📷'),
('Alfombrillas',  'alfombrillas', '🟫');

-- Marcas
INSERT INTO marcas (nombre) VALUES
('Razer'),('Logitech'),('SteelSeries'),('Corsair'),('HyperX'),('ASUS ROG'),('Redragon'),('Secretlab');

-- Productos
INSERT INTO productos (nombre, descripcion, especificaciones, precio, precio_anterior, stock, imagen, categoria_id, marca_id, badge) VALUES
('DeathAdder V3 Pro',     'Mouse inalámbrico profesional ultra ligero para esports',      '30000 DPI · Wireless · 63g · Focus Pro',           89990,  109990, 50, '🖱️', 2, 1, 'hot'),
('Apex Pro TKL',          'Teclado mecánico TKL con switches OLED ajustables',            'Switches OLED · RGB · Mecánico · TKL · 8000Hz',    179990, NULL,   30, '⌨️', 1, 3, 'new'),
('K100 RGB Airspeed',     'Teclado gaming de alto rendimiento Axon v2',                  'Axon v2 · Cherry MX · Titanium · RGB',             139990, 169990, 25, '⌨️', 1, 4, 'sale'),
('Cloud Alpha S',         'Headset gaming con audio virtual 7.1 surround',               '7.1 Virtual · Desmontable · 309g · USB',            79990, NULL,   40, '🎧', 3, 5, NULL),
('GPW 3 Superlight',      'El mouse inalámbrico más ligero del mercado gaming',          'Hero 25K · 61g · LIGHTSPEED · USB-C',              119990, 139990, 60, '🖱️', 2, 2, 'hot'),
('ROG Swift 27" 165Hz',   'Monitor gaming 2K IPS con sincronización adaptativa',        '2K IPS · HDR400 · 1ms · G-Sync · 165Hz',           399990, NULL,   15, '🖥️', 4, 6, 'new'),
('Arctis Nova Pro',       'Headset premium con cancelación activa de ruido',             'Hi-Res · ANC · Base Station Gen 2',                249990, 299990, 20, '🎧', 3, 3, 'sale'),
('Viper V3 Pro',          'Mouse de competición inalámbrico ultra ligero',               '35K DPI · 54g · Focus Pro Optical · RGB',          149990, NULL,   35, '🖱️', 2, 1, 'new'),
('Titan Evo 2022 L',      'Silla gaming premium con material SoftWeave transpirable',   'SoftWeave · 4D Armrest · L size · Reclinable 165°',549990, 699990, 10, '🪑', 5, 8, 'hot'),
('Huntsman V3 Pro',       'Teclado con switches ópticos analógicos de alta velocidad',  'Analog Optical · Chroma RGB · 8000Hz · USB',       159990, NULL,   28, '⌨️', 1, 1, 'new'),
('G Pro X Superlight 2',  'Mouse esports con tecnología LIGHTFORCE',                    'Hero 2 25.6K · 60g · LIGHTFORCE · Wireless',       129990, 149990, 45, '🖱️', 2, 2, 'sale'),
('Kraken V3 HyperSense',  'Headset con retroalimentación háptica THX',                  'Haptic · THX 7.1 · RGB · USB-C · 50mm',            119990, NULL,   22, '🎧', 3, 1, NULL);
