USE alumipro_db;

INSERT INTO clientes (nombre, telefono, direccion, email) VALUES
  ('Carlos Pérez', '3000000000', 'Calle 10 #20-30', 'carlos@mail.com'),
  ('Johan Sánchez', '3001234567', 'Calle 10 #20-30', 'johan@sena.com'),
  ('Andrea Murcia', '3214569870', NULL, 'andrea@gmail.com')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

INSERT INTO productos (nombre, precio, stock, descripcion) VALUES
  ('Aluminio perfil 2m', 98000.00, 49, 'Perfil aluminio para ventanería fina'),
  ('Vidrio templado 8mm', 210000.00, 8, 'Vidrio templado para fachada'),
  ('Ventanería termoacústica', 1500000.00, 53, 'Ventanería termoacústica para mitigar ruidos exteriores'),
  ('Vidrio tipo espejo', 85000.00, 200, 'Vidrio tipo espejo para decoraciones'),
  ('Aluminio estructural', 280000.00, 499, 'Aluminio estructural tipo parante para estructuras')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);
