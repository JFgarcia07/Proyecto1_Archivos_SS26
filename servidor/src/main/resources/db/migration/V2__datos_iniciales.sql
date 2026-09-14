INSERT INTO Rol (nombre_rol, permisos) VALUES
    ('Administrador', 'ADMIN'),
    ('Encargado de Compras', 'COMPRAS'),
    ('Encargado de Inventario', 'INVENTARIO'),
    ('Encargado de Ventas', 'VENTAS');

INSERT INTO Usuario (id_usuario, nombre_usuario, email, password, id_rol, activo) VALUES
    ('U0001', 'Admin General', 'admin@minierp.com',
     '$2b$10$cZKypEwLggE7c98D.4g5nO2rW1KzO.mTf5jruaotEQIOTe33HJWZW',
     (SELECT id_rol FROM Rol WHERE permisos = 'ADMIN'), TRUE),
 
    ('U0002', 'Encargado Compras', 'compras@minierp.com',
     '$2b$10$gi3KA40l4nxBuGiuQKLleuKhhcLC8zfy2UJtjkpqiT91Kn1yK66vC',
     (SELECT id_rol FROM Rol WHERE permisos = 'COMPRAS'), TRUE),
 
    ('U0003', 'Encargado Inventario', 'inventario@minierp.com',
     '$2b$10$htlmfv35EVoLjGqFQ4ql3euFVWUB/zvl/tdIZdnIjwQZJnroBjrXe',
     (SELECT id_rol FROM Rol WHERE permisos = 'INVENTARIO'), TRUE),
 
    ('U0004', 'Encargado Ventas', 'ventas@minierp.com',
     '$2b$10$y0HULrqw3DQsqGBdrtFhzOIL5nTWEGUYs9GEmNPgs/mnZla.173C.',
     (SELECT id_rol FROM Rol WHERE permisos = 'VENTAS'), TRUE);