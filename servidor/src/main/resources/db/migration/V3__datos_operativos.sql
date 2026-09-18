
-- ============ PROVEEDORES ============
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV001', 'Muebles del Norte S.A.', 'Zona 4, Ciudad de Guatemala', TRUE);
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV002', 'Maderas y Diseños Ltda.', 'Xela, Quetzaltenango', TRUE);
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV003', 'Importadora Mobiliaria GT', 'Zona 10, Ciudad de Guatemala', TRUE);
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV004', 'Distribuidora El Roble', 'Escuintla', TRUE);
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV005', 'Oficinas Modernas S.A.', 'Zona 9, Ciudad de Guatemala', TRUE);
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV006', 'Fábrica de Muebles Antigua', 'Antigua Guatemala', TRUE);
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV007', 'Confort Home Import', 'Mixco', TRUE);
INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES ('PV008', 'Estilo & Madera', 'Cobán, Alta Verapaz', TRUE);

-- ============ PRODUCTOS (stock inicia en 0, se llena con las compras) ============
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR001', 'Escritorio Ejecutivo', 'Escritorios', 1460.82, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR002', 'Escritorio en L', 'Escritorios', 201.27, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR003', 'Escritorio Minimalista', 'Escritorios', 713.81, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR004', 'Escritorio Gamer', 'Escritorios', 607.58, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR005', 'Escritorio Infantil', 'Escritorios', 1659.77, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR006', 'Silla Ergonómica', 'Sillas', 1537.23, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR007', 'Silla Ejecutiva', 'Sillas', 1978.97, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR008', 'Silla de Visita', 'Sillas', 328.22, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR009', 'Silla Gamer', 'Sillas', 1014.94, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR010', 'Silla Plegable', 'Sillas', 211.08, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR011', 'Mesa de Centro', 'Mesas', 598.21, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR012', 'Mesa de Comedor 6p', 'Mesas', 1185.98, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR013', 'Mesa Auxiliar', 'Mesas', 204.40, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR014', 'Mesa de Reuniones', 'Mesas', 557.62, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR015', 'Mesa Plegable', 'Mesas', 1482.26, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR016', 'Estantería Metálica', 'Estanterías', 1267.13, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR017', 'Estantería de Madera', 'Estanterías', 601.90, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR018', 'Librero 5 Niveles', 'Estanterías', 1357.99, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR019', 'Estantería Modular', 'Estanterías', 1809.33, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR020', 'Estantería Esquinera', 'Estanterías', 163.32, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR021', 'Sofá 2 Plazas', 'Sofás', 1801.93, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR022', 'Sofá 3 Plazas', 'Sofás', 1581.19, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR023', 'Sofá Seccional', 'Sofás', 847.51, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR024', 'Sofá Cama', 'Sofás', 468.73, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR025', 'Sillón Reclinable', 'Sofás', 2112.29, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR026', 'Lámpara de Escritorio', 'Accesorios', 840.02, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR027', 'Organizador de Oficina', 'Accesorios', 340.13, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR028', 'Perchero de Pie', 'Accesorios', 348.27, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR029', 'Cojín Decorativo', 'Accesorios', 1887.36, 0, TRUE);
INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES ('PR030', 'Alfombra de Oficina', 'Accesorios', 1387.64, 0, TRUE);

-- ============ PRODUCTO_PROVEEDOR (relacion N:M) ============
-- Cada producto queda asociado al proveedor de su compra inicial;
-- algunos productos llevan un segundo proveedor alterno, para reflejar N:M real.
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR001', 'PV001', 949.53);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR002', 'PV001', 130.83);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR003', 'PV002', 463.98);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR004', 'PV002', 394.93);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR005', 'PV003', 1078.85);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR006', 'PV003', 999.20);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR007', 'PV004', 1286.33);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR008', 'PV004', 213.34);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR009', 'PV005', 659.71);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR010', 'PV005', 137.20);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR011', 'PV006', 388.84);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR012', 'PV006', 770.89);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR013', 'PV007', 132.86);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR014', 'PV007', 362.45);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR015', 'PV008', 963.47);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR016', 'PV008', 823.63);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR017', 'PV001', 391.24);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR018', 'PV001', 882.69);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR019', 'PV002', 1176.06);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR020', 'PV002', 106.16);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR021', 'PV003', 1171.25);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR022', 'PV003', 1027.77);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR023', 'PV004', 550.88);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR024', 'PV004', 304.67);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR025', 'PV005', 1372.99);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR026', 'PV005', 546.01);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR027', 'PV006', 221.08);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR028', 'PV006', 226.38);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR029', 'PV007', 1226.78);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR030', 'PV007', 901.97);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR001', 'PV004', 1022.57);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR006', 'PV001', 1076.06);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR016', 'PV003', 886.99);
INSERT INTO ProductoProveedor (id_producto, id_proveedor, precio_proveedor) VALUES ('PR021', 'PV008', 1261.35);

-- ============ CLIENTES ============
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Ana Lucía Pérez', '1728977-7', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Carlos Ramírez', '9996414-1', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('María José López', '7350753-1', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Juan Carlos Gómez', '5918715-9', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Fernanda Castillo', '7067228-9', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Luis Enrique Morales', '4226067-1', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Gabriela Sánchez', '1768805-3', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Roberto Herrera', '5855124-1', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Daniela Cifuentes', '4905582-1', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Miguel Ángel Ruiz', '7377459-4', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Andrea Solís', '8606962-5', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('José Antonio Vásquez', '3728882-5', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Paola Rodríguez', '6960453-3', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Diego Alejandro Paz', '5479144-1', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Lucía Fernández', '3871230-8', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Óscar Iván Chávez', '5107245-2', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Silvia Marroquín', '8755439-6', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Ricardo Estrada', '5528972-8', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Karla Beatriz Ortiz', '4684531-5', 0, TRUE);
INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES ('Hugo René Aguilar', '1938483-3', 0, TRUE);

-- ============ COMPRAS + DETALLE_COMPRA + MOVIMIENTO (ENTRADA) ============
-- Todas las compras las registra el usuario U0002 (area de Compras).
INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (1, 'PV001', 'U0002', '2026-01-26', 21607.20);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (1, 'PR001', 20, 949.53);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR001', 'ENTRADA', 20, '2026-01-26', 'COMPRA', 1);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR001';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (1, 'PR002', 20, 130.83);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR002', 'ENTRADA', 20, '2026-01-26', 'COMPRA', 1);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR002';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (2, 'PV002', 'U0002', '2026-06-13', 17178.20);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (2, 'PR003', 20, 463.98);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR003', 'ENTRADA', 20, '2026-06-13', 'COMPRA', 2);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR003';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (2, 'PR004', 20, 394.93);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR004', 'ENTRADA', 20, '2026-06-13', 'COMPRA', 2);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR004';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (3, 'PV003', 'U0002', '2026-05-03', 41561.00);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (3, 'PR005', 20, 1078.85);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR005', 'ENTRADA', 20, '2026-05-03', 'COMPRA', 3);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR005';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (3, 'PR006', 20, 999.20);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR006', 'ENTRADA', 20, '2026-05-03', 'COMPRA', 3);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR006';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (4, 'PV004', 'U0002', '2026-04-19', 29993.40);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (4, 'PR007', 20, 1286.33);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR007', 'ENTRADA', 20, '2026-04-19', 'COMPRA', 4);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR007';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (4, 'PR008', 20, 213.34);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR008', 'ENTRADA', 20, '2026-04-19', 'COMPRA', 4);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR008';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (5, 'PV005', 'U0002', '2026-06-07', 15938.20);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (5, 'PR009', 20, 659.71);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR009', 'ENTRADA', 20, '2026-06-07', 'COMPRA', 5);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR009';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (5, 'PR010', 20, 137.20);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR010', 'ENTRADA', 20, '2026-06-07', 'COMPRA', 5);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR010';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (6, 'PV006', 'U0002', '2026-08-13', 23194.60);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (6, 'PR011', 20, 388.84);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR011', 'ENTRADA', 20, '2026-08-13', 'COMPRA', 6);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR011';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (6, 'PR012', 20, 770.89);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR012', 'ENTRADA', 20, '2026-08-13', 'COMPRA', 6);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR012';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (7, 'PV007', 'U0002', '2026-08-05', 9906.20);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (7, 'PR013', 20, 132.86);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR013', 'ENTRADA', 20, '2026-08-05', 'COMPRA', 7);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR013';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (7, 'PR014', 20, 362.45);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR014', 'ENTRADA', 20, '2026-08-05', 'COMPRA', 7);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR014';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (8, 'PV008', 'U0002', '2026-05-05', 35742.00);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (8, 'PR015', 20, 963.47);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR015', 'ENTRADA', 20, '2026-05-05', 'COMPRA', 8);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR015';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (8, 'PR016', 20, 823.63);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR016', 'ENTRADA', 20, '2026-05-05', 'COMPRA', 8);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR016';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (9, 'PV001', 'U0002', '2026-04-24', 25478.60);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (9, 'PR017', 20, 391.24);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR017', 'ENTRADA', 20, '2026-04-24', 'COMPRA', 9);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR017';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (9, 'PR018', 20, 882.69);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR018', 'ENTRADA', 20, '2026-04-24', 'COMPRA', 9);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR018';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (10, 'PV002', 'U0002', '2026-05-24', 25644.40);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (10, 'PR019', 20, 1176.06);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR019', 'ENTRADA', 20, '2026-05-24', 'COMPRA', 10);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR019';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (10, 'PR020', 20, 106.16);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR020', 'ENTRADA', 20, '2026-05-24', 'COMPRA', 10);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR020';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (11, 'PV003', 'U0002', '2026-07-19', 43980.40);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (11, 'PR021', 20, 1171.25);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR021', 'ENTRADA', 20, '2026-07-19', 'COMPRA', 11);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR021';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (11, 'PR022', 20, 1027.77);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR022', 'ENTRADA', 20, '2026-07-19', 'COMPRA', 11);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR022';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (12, 'PV004', 'U0002', '2026-07-12', 17111.00);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (12, 'PR023', 20, 550.88);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR023', 'ENTRADA', 20, '2026-07-12', 'COMPRA', 12);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR023';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (12, 'PR024', 20, 304.67);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR024', 'ENTRADA', 20, '2026-07-12', 'COMPRA', 12);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR024';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (13, 'PV005', 'U0002', '2026-04-05', 38380.00);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (13, 'PR025', 20, 1372.99);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR025', 'ENTRADA', 20, '2026-04-05', 'COMPRA', 13);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR025';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (13, 'PR026', 20, 546.01);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR026', 'ENTRADA', 20, '2026-04-05', 'COMPRA', 13);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR026';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (14, 'PV006', 'U0002', '2026-08-03', 8949.20);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (14, 'PR027', 20, 221.08);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR027', 'ENTRADA', 20, '2026-08-03', 'COMPRA', 14);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR027';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (14, 'PR028', 20, 226.38);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR028', 'ENTRADA', 20, '2026-08-03', 'COMPRA', 14);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR028';

INSERT INTO Compra (id_compra, id_proveedor, id_usuario, fecha, total) VALUES (15, 'PV007', 'U0002', '2026-01-28', 42575.00);
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (15, 'PR029', 20, 1226.78);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR029', 'ENTRADA', 20, '2026-01-28', 'COMPRA', 15);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR029';
INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) VALUES (15, 'PR030', 20, 901.97);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR030', 'ENTRADA', 20, '2026-01-28', 'COMPRA', 15);
UPDATE Producto SET stock = stock + 20 WHERE id_producto = 'PR030';

-- ============ VENTAS + DETALLE_VENTA + MOVIMIENTO (SALIDA) ============
-- Todas las ventas las registra el usuario U0004 (area de Ventas).
INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (1, 'U0004', 1, '2026-03-21', 1460.82, 175.30, 1636.12);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (1, 'PR001', 1, 1460.82);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR001', 'SALIDA', 1, '2026-03-21', 'VENTA', 1);
UPDATE Producto SET stock = stock - 1 WHERE id_producto = 'PR001';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (2, 'U0004', 2, '2026-07-20', 402.54, 48.30, 450.84);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (2, 'PR002', 2, 201.27);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR002', 'SALIDA', 2, '2026-07-20', 'VENTA', 2);
UPDATE Producto SET stock = stock - 2 WHERE id_producto = 'PR002';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (3, 'U0004', 3, '2026-07-13', 713.81, 85.66, 799.47);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (3, 'PR003', 1, 713.81);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR003', 'SALIDA', 1, '2026-07-13', 'VENTA', 3);
UPDATE Producto SET stock = stock - 1 WHERE id_producto = 'PR003';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (4, 'U0004', 4, '2026-05-18', 2430.32, 291.64, 2721.96);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (4, 'PR004', 4, 607.58);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR004', 'SALIDA', 4, '2026-05-18', 'VENTA', 4);
UPDATE Producto SET stock = stock - 4 WHERE id_producto = 'PR004';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (5, 'U0004', 5, '2026-02-22', 1659.77, 199.17, 1858.94);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (5, 'PR005', 1, 1659.77);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR005', 'SALIDA', 1, '2026-02-22', 'VENTA', 5);
UPDATE Producto SET stock = stock - 1 WHERE id_producto = 'PR005';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (6, 'U0004', 6, '2026-06-04', 4611.69, 553.40, 5165.09);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (6, 'PR006', 3, 1537.23);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR006', 'SALIDA', 3, '2026-06-04', 'VENTA', 6);
UPDATE Producto SET stock = stock - 3 WHERE id_producto = 'PR006';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (7, 'U0004', 7, '2026-07-06', 5936.91, 712.43, 6649.34);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (7, 'PR007', 3, 1978.97);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR007', 'SALIDA', 3, '2026-07-06', 'VENTA', 7);
UPDATE Producto SET stock = stock - 3 WHERE id_producto = 'PR007';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (8, 'U0004', 8, '2026-01-24', 1312.88, 157.55, 1470.43);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (8, 'PR008', 4, 328.22);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR008', 'SALIDA', 4, '2026-01-24', 'VENTA', 8);
UPDATE Producto SET stock = stock - 4 WHERE id_producto = 'PR008';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (9, 'U0004', 9, '2026-03-17', 3044.82, 365.38, 3410.20);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (9, 'PR009', 3, 1014.94);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR009', 'SALIDA', 3, '2026-03-17', 'VENTA', 9);
UPDATE Producto SET stock = stock - 3 WHERE id_producto = 'PR009';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (10, 'U0004', 10, '2026-05-27', 211.08, 25.33, 236.41);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (10, 'PR010', 1, 211.08);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR010', 'SALIDA', 1, '2026-05-27', 'VENTA', 10);
UPDATE Producto SET stock = stock - 1 WHERE id_producto = 'PR010';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (11, 'U0004', 11, '2026-03-12', 1196.42, 143.57, 1339.99);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (11, 'PR011', 2, 598.21);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR011', 'SALIDA', 2, '2026-03-12', 'VENTA', 11);
UPDATE Producto SET stock = stock - 2 WHERE id_producto = 'PR011';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (12, 'U0004', 12, '2026-01-20', 2371.96, 284.64, 2656.60);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (12, 'PR012', 2, 1185.98);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR012', 'SALIDA', 2, '2026-01-20', 'VENTA', 12);
UPDATE Producto SET stock = stock - 2 WHERE id_producto = 'PR012';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (13, 'U0004', 13, '2026-08-01', 613.20, 73.58, 686.78);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (13, 'PR013', 3, 204.40);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR013', 'SALIDA', 3, '2026-08-01', 'VENTA', 13);
UPDATE Producto SET stock = stock - 3 WHERE id_producto = 'PR013';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (14, 'U0004', 14, '2026-06-27', 557.62, 66.91, 624.53);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (14, 'PR014', 1, 557.62);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR014', 'SALIDA', 1, '2026-06-27', 'VENTA', 14);
UPDATE Producto SET stock = stock - 1 WHERE id_producto = 'PR014';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (15, 'U0004', 15, '2026-04-02', 4446.78, 533.61, 4980.39);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (15, 'PR015', 3, 1482.26);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR015', 'SALIDA', 3, '2026-04-02', 'VENTA', 15);
UPDATE Producto SET stock = stock - 3 WHERE id_producto = 'PR015';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (16, 'U0004', 16, '2026-02-03', 2534.26, 304.11, 2838.37);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (16, 'PR016', 2, 1267.13);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR016', 'SALIDA', 2, '2026-02-03', 'VENTA', 16);
UPDATE Producto SET stock = stock - 2 WHERE id_producto = 'PR016';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (17, 'U0004', 17, '2026-02-25', 2407.60, 288.91, 2696.51);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (17, 'PR017', 4, 601.90);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR017', 'SALIDA', 4, '2026-02-25', 'VENTA', 17);
UPDATE Producto SET stock = stock - 4 WHERE id_producto = 'PR017';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (18, 'U0004', 18, '2026-03-22', 2715.98, 325.92, 3041.90);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (18, 'PR018', 2, 1357.99);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR018', 'SALIDA', 2, '2026-03-22', 'VENTA', 18);
UPDATE Producto SET stock = stock - 2 WHERE id_producto = 'PR018';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (19, 'U0004', 19, '2026-03-09', 7237.32, 868.48, 8105.80);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (19, 'PR019', 4, 1809.33);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR019', 'SALIDA', 4, '2026-03-09', 'VENTA', 19);
UPDATE Producto SET stock = stock - 4 WHERE id_producto = 'PR019';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (20, 'U0004', 20, '2026-04-18', 653.28, 78.39, 731.67);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (20, 'PR020', 4, 163.32);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR020', 'SALIDA', 4, '2026-04-18', 'VENTA', 20);
UPDATE Producto SET stock = stock - 4 WHERE id_producto = 'PR020';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (21, 'U0004', 1, '2026-05-13', 3603.86, 432.46, 4036.32);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (21, 'PR021', 2, 1801.93);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR021', 'SALIDA', 2, '2026-05-13', 'VENTA', 21);
UPDATE Producto SET stock = stock - 2 WHERE id_producto = 'PR021';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (22, 'U0004', 2, '2026-08-17', 4743.57, 569.23, 5312.80);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (22, 'PR022', 3, 1581.19);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR022', 'SALIDA', 3, '2026-08-17', 'VENTA', 22);
UPDATE Producto SET stock = stock - 3 WHERE id_producto = 'PR022';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (23, 'U0004', 3, '2026-02-08', 3390.04, 406.80, 3796.84);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (23, 'PR023', 4, 847.51);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR023', 'SALIDA', 4, '2026-02-08', 'VENTA', 23);
UPDATE Producto SET stock = stock - 4 WHERE id_producto = 'PR023';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (24, 'U0004', 4, '2026-02-11', 937.46, 112.50, 1049.96);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (24, 'PR024', 2, 468.73);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR024', 'SALIDA', 2, '2026-02-11', 'VENTA', 24);
UPDATE Producto SET stock = stock - 2 WHERE id_producto = 'PR024';

INSERT INTO Venta (id_venta, id_usuario, id_cliente, fecha, subtotal, iva, total) VALUES (25, 'U0004', 5, '2026-04-19', 2112.29, 253.47, 2365.76);
INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) VALUES (25, 'PR025', 1, 2112.29);
INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) VALUES ('PR025', 'SALIDA', 1, '2026-04-19', 'VENTA', 25);
UPDATE Producto SET stock = stock - 1 WHERE id_producto = 'PR025';

-- ============ AJUSTE DE SECUENCIAS (para que futuros INSERT no choquen con estos IDs) ============
SELECT setval(pg_get_serial_sequence('Cliente', 'id_cliente'), (SELECT MAX(id_cliente) FROM Cliente));
SELECT setval(pg_get_serial_sequence('Compra', 'id_compra'), (SELECT MAX(id_compra) FROM Compra));
SELECT setval(pg_get_serial_sequence('Venta', 'id_venta'), (SELECT MAX(id_venta) FROM Venta));
SELECT setval(pg_get_serial_sequence('MovimientoInventario', 'id_movimiento'), (SELECT MAX(id_movimiento) FROM MovimientoInventario));