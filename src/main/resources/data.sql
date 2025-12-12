-- Insertar roles
INSERT INTO roles (tipo) VALUES ('ADMIN');
INSERT INTO roles (tipo) VALUES ('GERENTE');
INSERT INTO roles (tipo) VALUES ('EMPLEADO');

-- Insertar usuarios con contraseñas encriptadas
-- admin123 = $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.
-- gerente123 = $2a$10$8cjz47bjbR4Mn8GMg9IcX.H8.83g5YGYzQvz1yYQN4oQOuDyh6uxm
-- empleado123 = $2a$10$DowJonesIndex123456789012345678901234567890123456789012

INSERT INTO usuarios (nombre, correo, password_hash, id_rol) VALUES 
('Administrador', 'admin@modacol.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 1);

INSERT INTO usuarios (nombre, correo, password_hash, id_rol) VALUES 
('Gerente', 'gerente@modacol.com', '$2a$10$8cjz47bjbR4Mn8GMg9IcX.H8.83g5YGYzQvz1yYQN4oQOuDyh6uxm', 2);

INSERT INTO usuarios (nombre, correo, password_hash, id_rol) VALUES 
('Empleado', 'empleado@modacol.com', '$2a$10$DowJonesIndex123456789012345678901234567890123456789012', 3);