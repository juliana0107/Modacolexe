-- Script para agregar columna 'activo' a la tabla usuarios
-- Ejecutar este script si la aplicación ya está corriendo y necesitas actualizar la BD manualmente

-- Agregar columna activo a usuarios
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;

-- Actualizar registros existentes para que estén activos
UPDATE usuarios SET activo = true WHERE activo IS NULL;
