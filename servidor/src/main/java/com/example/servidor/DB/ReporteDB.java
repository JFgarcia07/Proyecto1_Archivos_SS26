package com.example.servidor.DB;

import java.sql.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.servidor.Model.Compra;
import com.example.servidor.Model.ReporteFila;
import com.example.servidor.Model.ResumenPeriodo;
import com.example.servidor.Model.Venta;

/**
 * Consultas SQL del modulo de reportes.
 *
 * Todas las consultas filtran por un rango de fechas con
 * BETWEEN ? AND ?. Cuando el usuario no elige fechas, el servicio
 * envia un rango muy amplio para que se incluyan todos los registros.
 *
 * @author jfgarcianata
 */
@Repository
public class ReporteDB {

    private final JdbcTemplate jdbcTemplate;

    private final String QUERY_MAS_VENDIDOS = """
        SELECT p.id_producto     AS codigo,
               p.nombre_producto AS nombre,
               p.categoria       AS detalle,
               SUM(dv.cantidad)  AS cantidad,
               COUNT(*)          AS conteo,
               SUM(dv.cantidad * dv.precio_unitario) AS monto
        FROM DetalleVenta dv
        INNER JOIN Venta v    ON dv.id_venta = v.id_venta
        INNER JOIN Producto p ON dv.id_producto = p.id_producto
        WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY p.id_producto, p.nombre_producto, p.categoria
        ORDER BY SUM(dv.cantidad) DESC
        LIMIT 10
        """;

    private final String QUERY_MENOR_EXISTENCIA = """
        SELECT id_producto     AS codigo,
               nombre_producto AS nombre,
               categoria       AS detalle,
               stock           AS cantidad,
               0               AS conteo,
               precio          AS monto
        FROM Producto
        WHERE activo = TRUE
        ORDER BY stock ASC
        LIMIT 10
        """;

    private final String QUERY_MAS_MOVIMIENTOS = """
        SELECT p.id_producto     AS codigo,
               p.nombre_producto AS nombre,
               p.categoria       AS detalle,
               SUM(m.cantidad)   AS cantidad,
               COUNT(*)          AS conteo,
               0                 AS monto
        FROM MovimientoInventario m
        INNER JOIN Producto p ON m.id_producto = p.id_producto
        WHERE CAST(m.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY p.id_producto, p.nombre_producto, p.categoria
        ORDER BY COUNT(*) DESC
        LIMIT 10
        """;

    private final String QUERY_COMPRAS = """
        SELECT c.*, p.nombre_proveedor
        FROM Compra c
        INNER JOIN Proveedor p ON c.id_proveedor = p.id_proveedor
        WHERE CAST(c.fecha AS DATE) BETWEEN ? AND ?
        ORDER BY c.fecha DESC
        """;

    private final String QUERY_TOP_PROVEEDORES = """
        SELECT pv.id_proveedor     AS codigo,
               pv.nombre_proveedor AS nombre,
               pv.ubicacion        AS detalle,
               0                   AS cantidad,
               COUNT(*)            AS conteo,
               SUM(c.total)        AS monto
        FROM Compra c
        INNER JOIN Proveedor pv ON c.id_proveedor = pv.id_proveedor
        WHERE CAST(c.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY pv.id_proveedor, pv.nombre_proveedor, pv.ubicacion
        ORDER BY SUM(c.total) DESC
        LIMIT 5
        """;

    private final String QUERY_MAS_ADQUIRIDOS = """
        SELECT p.id_producto     AS codigo,
               p.nombre_producto AS nombre,
               p.categoria       AS detalle,
               SUM(dc.cantidad)  AS cantidad,
               COUNT(*)          AS conteo,
               SUM(dc.cantidad * dc.precio_unitario) AS monto
        FROM DetalleCompra dc
        INNER JOIN Compra c   ON dc.id_compra = c.id_compra
        INNER JOIN Producto p ON dc.id_producto = p.id_producto
        WHERE CAST(c.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY p.id_producto, p.nombre_producto, p.categoria
        ORDER BY COUNT(*) DESC, SUM(dc.cantidad) DESC
        LIMIT 10
        """;

    private final String QUERY_VENTAS = """
        SELECT v.*, c.nombre, c.nit, u.nombre_usuario
        FROM Venta v
        INNER JOIN Cliente c ON v.id_cliente = c.id_cliente
        INNER JOIN Usuario u ON v.id_usuario = u.id_usuario
        WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
        ORDER BY v.fecha DESC
        """;

    private final String QUERY_TOP_CLIENTES = """
        SELECT cl.id_cliente AS codigo,
               cl.nombre     AS nombre,
               cl.nit        AS detalle,
               0             AS cantidad,
               COUNT(*)      AS conteo,
               SUM(v.total)  AS monto
        FROM Venta v
        INNER JOIN Cliente cl ON v.id_cliente = cl.id_cliente
        WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY cl.id_cliente, cl.nombre, cl.nit
        ORDER BY SUM(v.total) DESC
        LIMIT 10
        """;

    private final String QUERY_MAYORES_INGRESOS = """
        SELECT p.id_producto     AS codigo,
               p.nombre_producto AS nombre,
               p.categoria       AS detalle,
               SUM(dv.cantidad)  AS cantidad,
               COUNT(*)          AS conteo,
               SUM(dv.cantidad * dv.precio_unitario) AS monto
        FROM DetalleVenta dv
        INNER JOIN Venta v    ON dv.id_venta = v.id_venta
        INNER JOIN Producto p ON dv.id_producto = p.id_producto
        WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY p.id_producto, p.nombre_producto, p.categoria
        ORDER BY SUM(dv.cantidad * dv.precio_unitario) DESC
        LIMIT 10
        """;

    private final String QUERY_RESUMEN_DIA = """
        SELECT TO_CHAR(v.fecha, 'YYYY-MM-DD') AS periodo,
               COUNT(*)        AS numero_ventas,
               SUM(v.subtotal) AS subtotal,
               SUM(v.iva)      AS iva,
               SUM(v.total)    AS total
        FROM Venta v
        WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY TO_CHAR(v.fecha, 'YYYY-MM-DD')
        ORDER BY TO_CHAR(v.fecha, 'YYYY-MM-DD')
        """;

    private final String QUERY_RESUMEN_MES = """
        SELECT TO_CHAR(v.fecha, 'YYYY-MM') AS periodo,
               COUNT(*)        AS numero_ventas,
               SUM(v.subtotal) AS subtotal,
               SUM(v.iva)      AS iva,
               SUM(v.total)    AS total
        FROM Venta v
        WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY TO_CHAR(v.fecha, 'YYYY-MM')
        ORDER BY TO_CHAR(v.fecha, 'YYYY-MM')
        """;

    private final String QUERY_RESUMEN_ANIO = """
        SELECT TO_CHAR(v.fecha, 'YYYY') AS periodo,
               COUNT(*)        AS numero_ventas,
               SUM(v.subtotal) AS subtotal,
               SUM(v.iva)      AS iva,
               SUM(v.total)    AS total
        FROM Venta v
        WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
        GROUP BY TO_CHAR(v.fecha, 'YYYY')
        ORDER BY TO_CHAR(v.fecha, 'YYYY')
        """;

    public ReporteDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReporteFila> productosMasVendidos(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_MAS_VENDIDOS, new ReporteFilaRowMapper(), fechaInicio, fechaFin);
    }

    public List<ReporteFila> productosMenorExistencia() {
        return jdbcTemplate.query(QUERY_MENOR_EXISTENCIA, new ReporteFilaRowMapper());
    }

    public List<ReporteFila> productosMasMovimientos(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_MAS_MOVIMIENTOS, new ReporteFilaRowMapper(), fechaInicio, fechaFin);
    }

    public List<Compra> comprasPorRango(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_COMPRAS, new CompraRowMapper(), fechaInicio, fechaFin);
    }

    public List<ReporteFila> topProveedores(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_TOP_PROVEEDORES, new ReporteFilaRowMapper(), fechaInicio, fechaFin);
    }

    public List<ReporteFila> productosMasAdquiridos(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_MAS_ADQUIRIDOS, new ReporteFilaRowMapper(), fechaInicio, fechaFin);
    }

    // ============ VENTAS Y CLIENTES ============

    public List<Venta> ventasPorRango(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_VENTAS, new VentaRowMapper(), fechaInicio, fechaFin);
    }

    public List<ReporteFila> topClientes(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_TOP_CLIENTES, new ReporteFilaRowMapper(), fechaInicio, fechaFin);
    }

    public List<ReporteFila> productosMayoresIngresos(Date fechaInicio, Date fechaFin) {
        return jdbcTemplate.query(QUERY_MAYORES_INGRESOS, new ReporteFilaRowMapper(), fechaInicio, fechaFin);
    }

    /**
     * Elige la consulta segun la agrupacion pedida (dia, mes o anio).
     */
    public List<ResumenPeriodo> resumenVentas(String agrupacion, Date fechaInicio, Date fechaFin) {
        String consulta;

        if ("DIA".equals(agrupacion)) {
            consulta = QUERY_RESUMEN_DIA;
        } else if ("ANIO".equals(agrupacion)) {
            consulta = QUERY_RESUMEN_ANIO;
        } else {
            consulta = QUERY_RESUMEN_MES;
        }

        return jdbcTemplate.query(consulta, new ResumenPeriodoRowMapper(), fechaInicio, fechaFin);
    }
}