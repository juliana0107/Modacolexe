-- Script para agregar columna 'activo' a la tabla categorias
-- Ejecutar este script si la aplicación ya está corriendo y necesitas actualizar la BD manualmente

-- Agregar columna activo a categorias
ALTER TABLE categorias ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT true;

-- Actualizar registros existentes para que estén activos
UPDATE categorias SET activo = true WHERE activo IS NULL;
