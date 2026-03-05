-- Script consolidado para agregar columna 'activo' a todas las tablas
-- Ejecutar este script si la aplicación ya está corriendo y necesitas actualizar la BD manualmente

-- Agregar columna activo a clientes
ALTER TABLE clientes ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;
UPDATE clientes SET activo = true WHERE activo IS NULL;

-- Agregar columna activo a productos
ALTER TABLE productos ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;
UPDATE productos SET activo = true WHERE activo IS NULL;

-- Agregar columna activo a proveedores
ALTER TABLE proveedores ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;
UPDATE proveedores SET activo = true WHERE activo IS NULL;

-- Agregar columna activo a compras
ALTER TABLE compras ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;
UPDATE compras SET activo = true WHERE activo IS NULL;

-- Agregar columna activo a ventas
ALTER TABLE ventas ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;
UPDATE ventas SET activo = true WHERE activo IS NULL;

-- Agregar columna activo a roles
ALTER TABLE roles ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;
UPDATE roles SET activo = true WHERE activo IS NULL;
