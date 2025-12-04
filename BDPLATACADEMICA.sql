DROP DATABASE IF EXISTS plataforma;
CREATE DATABASE plataforma
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
USE plataforma;

-- Tabla roles
DROP TABLE IF EXISTS rol;
CREATE TABLE rol (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE KEY ux_rol_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla usuarios
DROP TABLE IF EXISTS usuario;
CREATE TABLE usuario (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  apellido VARCHAR(150) NOT NULL,
  email VARCHAR(200) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  rol_id BIGINT UNSIGNED NOT NULL,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  KEY idx_usuario_rol (rol_id),

  CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id)
    REFERENCES rol (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos de ejemplo
INSERT INTO rol (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('PROFESOR', 'Usuario con permisos de consulta'),
('ESTUDIANTE', 'Acceso limitado a información personal');

INSERT INTO usuario (nombre, apellido, email, password, rol_id) VALUES
('Carlos', 'Ramírez', 'c.ramirez@correo.com', '12345', 1),
('Ana', 'Soto', 'ana.soto@correo.com', '12345', 3),
('Luisa', 'Fernández', 'l.fernandez@correo.com', '12345', 2);
