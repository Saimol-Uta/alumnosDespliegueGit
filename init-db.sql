-- Inicialización de la base de datos
-- Este script se ejecuta automáticamente al crear el contenedor MySQL

USE soa;

-- Crear la tabla 'usuarios' si no existe (compatible con la entidad `Usuario`)
CREATE TABLE IF NOT EXISTS usuarios (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(100) NOT NULL UNIQUE,
	nombre VARCHAR(100) NOT NULL,
	rol VARCHAR(20) NOT NULL DEFAULT 'SECRETARIA',
	activo BOOLEAN DEFAULT TRUE
);

-- NOTA: El primer usuario que se registre en la aplicación será ADMIN automáticamente.
-- Los usuarios siguientes serán SECRETARIA por defecto.
