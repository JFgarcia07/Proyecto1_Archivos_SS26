CREATE TABLE IF NOT EXISTS Rol (
    id_rol SERIAL PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    permisos VARCHAR(20) NOT NULL CHECK (permisos IN ('ADMIN','COMPRAS','INVENTARIO','VENTAS'))
);

CREATE TABLE IF NOT EXISTS Usuario (
    id_usuario VARCHAR(5) PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL,
    email VARCHAR(320) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    id_rol INTEGER NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_id_rol FOREIGN KEY (id_rol) REFERENCES Rol(id_rol)
);

CREATE TABLE IF NOT EXISTS Producto (
    id_producto VARCHAR(5) PRIMARY KEY,
    nombre_producto VARCHAR(150) NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    precio NUMERIC(12,2) NOT NULL CHECK (precio >= 0),
    stock NUMERIC(12,2) NOT NULL CHECK (stock >= 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS Proveedor (
    id_proveedor VARCHAR(5) PRIMARY KEY,
    nombre_proveedor VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(250),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS ProductoProveedor (
    id_producto VARCHAR(5) NOT NULL,
    id_proveedor VARCHAR(5) NOT NULL,
    precio_proveedor NUMERIC(12,2) CHECK (precio_proveedor >= 0),
    PRIMARY KEY (id_producto, id_proveedor),
    CONSTRAINT fk_id_producto_PP FOREIGN KEY (id_producto) REFERENCES Producto(id_producto),
    CONSTRAINT fk_id_proveedor_PP FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE IF NOT EXISTS Cliente (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    nit VARCHAR(9),
    saldo NUMERIC(12,2) NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS Compra (
    id_compra SERIAL PRIMARY KEY,
    id_proveedor VARCHAR(5) NOT NULL,
    id_usuario VARCHAR(5) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT now(),
    total NUMERIC(12,2) NOT NULL CHECK (total >= 0),
    CONSTRAINT fk_id_proveedor_C FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor),
    CONSTRAINT fk_id_usuario_C FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS DetalleCompra (
    id_compra INTEGER NOT NULL,
    id_producto VARCHAR(5) NOT NULL,
    cantidad NUMERIC(12,2) NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL CHECK (precio_unitario >= 0),
    PRIMARY KEY (id_compra, id_producto),
    CONSTRAINT fk_id_compra_DC FOREIGN KEY (id_compra) REFERENCES Compra(id_compra),
    CONSTRAINT fk_id_producto_DC FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);

CREATE TABLE IF NOT EXISTS Venta (
    id_venta SERIAL PRIMARY KEY,
    id_usuario VARCHAR(5) NOT NULL,
    id_cliente INTEGER NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT now(),
    subtotal NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0),
    iva NUMERIC(12,2) NOT NULL CHECK (iva >= 0),
    total NUMERIC(12,2) NOT NULL CHECK (total >= 0),
    CONSTRAINT fk_id_usuario_V FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    CONSTRAINT fk_id_cliente_V FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS DetalleVenta (
    id_venta INTEGER NOT NULL,
    id_producto VARCHAR(5) NOT NULL,
    cantidad NUMERIC(12,2) NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL CHECK (precio_unitario >= 0),
    PRIMARY KEY (id_venta, id_producto),
    CONSTRAINT fk_id_producto_DV FOREIGN KEY (id_producto) REFERENCES Producto(id_producto),
    CONSTRAINT fk_id_venta_DV FOREIGN KEY (id_venta) REFERENCES Venta(id_venta)
);

CREATE TABLE IF NOT EXISTS MovimientoInventario (
    id_movimiento SERIAL PRIMARY KEY,
    id_producto VARCHAR(5) NOT NULL,
    tipo_movimiento VARCHAR(10) NOT NULL CHECK (tipo_movimiento IN ('ENTRADA','SALIDA')),
    cantidad NUMERIC(12,2) NOT NULL CHECK (cantidad > 0),
    fecha TIMESTAMP NOT NULL DEFAULT now(),
    origen VARCHAR(10) NOT NULL CHECK (origen IN ('COMPRA','VENTA')),
    id_referencia INTEGER NOT NULL,
    CONSTRAINT fk_id_producto_MI FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);

CREATE TABLE IF NOT EXISTS LogSistema (
    id_log SERIAL PRIMARY KEY,
    id_usuario VARCHAR(5) NOT NULL,
    accion VARCHAR(50) NOT NULL,
    modulo VARCHAR(50) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT now(),
    detalle VARCHAR(500),
    CONSTRAINT fk_id_usuario_LS FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

-- Índices de apoyo para reportes
CREATE INDEX idx_compra_fecha ON Compra(fecha);
CREATE INDEX idx_venta_fecha ON Venta(fecha);
CREATE INDEX idx_movimiento_producto ON MovimientoInventario(id_producto);
CREATE INDEX idx_log_usuario ON LogSistema(id_usuario);