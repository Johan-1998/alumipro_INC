CREATE DATABASE IF NOT EXISTS alumipro_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE alumipro_db;

CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  correo VARCHAR(120) NOT NULL,
  password VARCHAR(255) NOT NULL,
  rol ENUM('ADMIN','VENDEDOR') NOT NULL DEFAULT 'VENDEDOR',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_usuarios_correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS clientes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  telefono VARCHAR(20) NULL,
  direccion VARCHAR(160) NULL,
  email VARCHAR(100) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS productos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  precio DECIMAL(12,2) NOT NULL,
  stock INT NOT NULL,
  descripcion TEXT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ventas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  cliente_id INT NOT NULL,
  vendedor_id INT NULL,
  total DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_ventas_cliente
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_ventas_vendedor
    FOREIGN KEY (vendedor_id) REFERENCES usuarios(id)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_ventas_cliente_id ON ventas(cliente_id);
CREATE INDEX idx_ventas_vendedor_id ON ventas(vendedor_id);

CREATE TABLE IF NOT EXISTS detalle_venta (
  id INT AUTO_INCREMENT PRIMARY KEY,
  venta_id INT NOT NULL,
  producto_id INT NOT NULL,
  cantidad INT NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_detalle_venta_venta
    FOREIGN KEY (venta_id) REFERENCES ventas(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_detalle_venta_producto
    FOREIGN KEY (producto_id) REFERENCES productos(id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_detalle_venta_venta_id ON detalle_venta(venta_id);
CREATE INDEX idx_detalle_venta_producto_id ON detalle_venta(producto_id);
