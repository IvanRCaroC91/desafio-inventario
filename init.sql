BEGIN;

CREATE TABLE IF NOT EXISTS usuarios (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  rol VARCHAR(50) NOT NULL DEFAULT 'ADMIN',
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS productos (
  id BIGSERIAL PRIMARY KEY,
  nombre VARCHAR(200) NOT NULL,
  sku VARCHAR(100) UNIQUE,
  precio NUMERIC(12,2) NOT NULL,
  stock INTEGER NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_productos_precio_nonneg CHECK (precio >= 0),
  CONSTRAINT chk_productos_stock_nonneg CHECK (stock >= 0)
);

CREATE TABLE IF NOT EXISTS ventas (
  id BIGSERIAL PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  fecha TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  total NUMERIC(12,2) NOT NULL DEFAULT 0,
  moneda VARCHAR(10) NOT NULL DEFAULT 'USD',
  estado VARCHAR(30) NOT NULL DEFAULT 'CONFIRMADA',
  CONSTRAINT fk_ventas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  CONSTRAINT chk_ventas_total_nonneg CHECK (total >= 0)
);

CREATE TABLE IF NOT EXISTS detalle_ventas (
  id BIGSERIAL PRIMARY KEY,
  venta_id BIGINT NOT NULL,
  producto_id BIGINT NOT NULL,
  cantidad INTEGER NOT NULL,
  precio_unitario NUMERIC(12,2) NOT NULL,
  subtotal NUMERIC(12,2) NOT NULL,
  CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
  CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id),
  CONSTRAINT chk_detalle_cantidad_pos CHECK (cantidad > 0),
  CONSTRAINT chk_detalle_precio_nonneg CHECK (precio_unitario >= 0),
  CONSTRAINT chk_detalle_subtotal_nonneg CHECK (subtotal >= 0)
);

INSERT INTO usuarios (username, password_hash, rol, activo)
VALUES ('admin', '$2a$10$3b4dqIgwIEhyEeC6Ic38kOBNZzWfDM61dloTS2Rw2fA7.etKpP2hC', 'ADMIN', TRUE)
ON CONFLICT (username) DO NOTHING;

INSERT INTO productos (nombre, sku, precio, stock, activo)
VALUES
  ('Laptop Pro 14', 'SKU-LAP-14', 1299.99, 10, TRUE),
  ('Mouse Inalambrico', 'SKU-MOU-001', 19.99, 150, TRUE),
  ('Teclado Mecanico', 'SKU-KEY-001', 79.90, 80, TRUE),
  ('Monitor 27" 2K', 'SKU-MON-27', 249.50, 35, TRUE),
  ('Audifonos Bluetooth', 'SKU-AUD-001', 59.99, 60, TRUE)
ON CONFLICT (sku) DO NOTHING;

COMMIT;
