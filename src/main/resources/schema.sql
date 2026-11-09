-- =============================================
-- Script de creación de la base de datos
-- Gestión de Usuarios - Arquitectura Hexagonal
-- =============================================

CREATE DATABASE IF NOT EXISTS crud_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_usuarios;

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'MEMBER', 'REVIEWER') NOT NULL,
    status      ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Usuario administrador inicial (password: Admin1234!)
INSERT INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$sfwkajsls.fktQb0Ngw5cOiSiV3z4LIwbucSAy4sw2wyNlxbUBt4q',
    'ADMIN',
    'ACTIVE'
);


-- =============================================
-- Ejercicio 25: Emisora
-- =============================================
CREATE TABLE IF NOT EXISTS emisoras (
    id             VARCHAR(36)   NOT NULL PRIMARY KEY,
    nombre         VARCHAR(100)  NOT NULL UNIQUE,
    canal          VARCHAR(50)   NOT NULL,
    banda_fm       DECIMAL(4,1)  NULL,
    banda_am       INT           NULL,
    num_locutores  INT           NOT NULL,
    genero         ENUM('NOTICIAS', 'MUSICAL', 'DEPORTIVA', 'CULTURAL', 'RELIGIOSA',
                        'EDUCATIVA', 'COMUNITARIA', 'VARIEDADES') NOT NULL,
    horario        VARCHAR(100)  NOT NULL,
    patrocinador   VARCHAR(150)  NULL,
    pais           VARCHAR(80)   NOT NULL,
    descripcion    VARCHAR(500)  NULL,
    num_programas  INT           NOT NULL DEFAULT 0,
    num_ciudades   INT           NOT NULL DEFAULT 1,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_emisora_banda CHECK (banda_fm IS NOT NULL OR banda_am IS NOT NULL),
    CONSTRAINT chk_emisora_fm CHECK (banda_fm IS NULL OR banda_fm BETWEEN 87.5 AND 108.0),
    CONSTRAINT chk_emisora_am CHECK (banda_am IS NULL OR banda_am BETWEEN 530 AND 1710),
    CONSTRAINT chk_emisora_locutores CHECK (num_locutores >= 1),
    CONSTRAINT chk_emisora_programas CHECK (num_programas >= 0),
    CONSTRAINT chk_emisora_ciudades CHECK (num_ciudades >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO emisoras (id, nombre, canal, banda_fm, banda_am, num_locutores, genero, horario,
                      patrocinador, pais, descripcion, num_programas, num_ciudades)
VALUES
    ('emi-001', 'Radio Cartagena Stereo', 'HJKC', 95.5, NULL, 6, 'MUSICAL', '24 horas',
     'Cervecería del Caribe', 'Colombia', 'Música tropical, salsa y vallenato', 12, 3),
    ('emi-002', 'Caracol Radio Cartagena', 'HJAN', 100.9, 1170, 10, 'NOTICIAS', '05:00 - 23:00',
     'Banco de Occidente', 'Colombia', 'Noticias locales y nacionales', 18, 5),
    ('emi-003', 'UdeC Radio', 'HJUC', 89.5, NULL, 4, 'EDUCATIVA', '06:00 - 22:00',
     'Universidad de Cartagena', 'Colombia', 'Emisora universitaria y cultural', 9, 1);
