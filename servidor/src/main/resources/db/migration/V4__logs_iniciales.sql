-- =========================================================
-- V4__logs_iniciales.sql
-- Registros iniciales de bitacora para demostrar el reporte de logs
-- =========================================================

INSERT INTO LogSistema (id_usuario, accion, modulo, fecha, detalle) VALUES
('U0001', 'INICIO_SESION', 'AUTENTICACION', '2026-08-01 08:05:00', 'Inicio de sesion: admin@minierp.com'),
('U0002', 'INICIO_SESION', 'AUTENTICACION', '2026-08-01 08:10:00', 'Inicio de sesion: compras@minierp.com'),
('U0002', 'CREAR',         'PROVEEDORES',   '2026-08-01 08:20:00', 'Creo el proveedor PV008 - Estilo & Madera'),
('U0002', 'REGISTRAR',     'COMPRAS',       '2026-08-01 09:00:00', 'Registro la compra No. 15 al proveedor PV007'),
('U0003', 'INICIO_SESION', 'AUTENTICACION', '2026-08-02 08:00:00', 'Inicio de sesion: inventario@minierp.com'),
('U0003', 'ACTUALIZAR',    'PRODUCTOS',     '2026-08-02 08:30:00', 'Actualizo el producto PR005 - Escritorio Infantil'),
('U0003', 'CREAR',         'PRODUCTOS',     '2026-08-02 09:15:00', 'Creo el producto PR030 - Alfombra de Oficina'),
('U0004', 'INICIO_SESION', 'AUTENTICACION', '2026-08-03 08:00:00', 'Inicio de sesion: ventas@minierp.com'),
('U0004', 'CREAR',         'CLIENTES',      '2026-08-03 08:40:00', 'Creo el cliente Hugo René Aguilar'),
('U0004', 'REGISTRAR',     'VENTAS',        '2026-08-03 10:00:00', 'Registro la venta No. 25 al cliente 5'),
('U0004', 'GENERAR_FACTURA','VENTAS',       '2026-08-03 10:01:00', 'Genero la factura de la venta No. 25'),
('U0001', 'INICIO_SESION', 'AUTENTICACION', '2026-08-04 07:55:00', 'Inicio de sesion: admin@minierp.com');