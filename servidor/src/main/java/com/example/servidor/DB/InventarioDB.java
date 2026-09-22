package com.example.servidor.DB;

import com.example.servidor.Model.MovimientoInventario;
import com.example.servidor.Model.Producto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InventarioDB {
    
    private final JdbcTemplate jdbcTemplate;
    public InventarioDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String QUERY_EXISTENCIAS =
        "SELECT * FROM Producto WHERE activo = TRUE ORDER BY stock ASC, id_producto";
    public List<Producto> listarExistencias() {
        return jdbcTemplate.query(QUERY_EXISTENCIAS, new ProductoRowMapper());
    }

    private final String QUERY_STOCK_BAJO =
        "SELECT * FROM Producto WHERE activo = TRUE AND stock <= ? ORDER BY stock ASC, id_producto";
    public List<Producto> listarStockBajo(double stockMinimo) {
        return jdbcTemplate.query(QUERY_STOCK_BAJO, new ProductoRowMapper(), stockMinimo);
    }

    /**
     * El historial une cada movimiento con la compra o venta que lo
     * genero, para mostrar el proveedor o cliente (tercero) y el
     * usuario que registro la operacion.
     */
    private final String QUERY_MOVIMIENTOS_BASE =
        "SELECT m.*, p.nombre_producto, " +
        "       COALESCE(pv.nombre_proveedor, cl.nombre) AS tercero, " +
        "       COALESCE(c.id_usuario, v.id_usuario) AS id_usuario " +
        "FROM MovimientoInventario m " +
        "INNER JOIN Producto p ON m.id_producto = p.id_producto " +
        "LEFT JOIN Compra c ON m.origen = 'COMPRA' AND m.id_referencia = c.id_compra " +
        "LEFT JOIN Proveedor pv ON c.id_proveedor = pv.id_proveedor " +
        "LEFT JOIN Venta v ON m.origen = 'VENTA' AND m.id_referencia = v.id_venta " +
        "LEFT JOIN Cliente cl ON v.id_cliente = cl.id_cliente " +
        "WHERE 1 = 1";
    public List<MovimientoInventario> listarMovimientos(String idProducto, String tipoMovimiento,
                                                        String fechaInicio, String fechaFin) {
        StringBuilder sql = new StringBuilder(QUERY_MOVIMIENTOS_BASE);
        List<Object> parametros = new ArrayList<>();

        if (idProducto != null && !idProducto.trim().isEmpty()) {
            sql.append(" AND m.id_producto = ?");
            parametros.add(idProducto);
        }

        if (tipoMovimiento != null && !tipoMovimiento.trim().isEmpty()) {
            sql.append(" AND m.tipo_movimiento = ?");
            parametros.add(tipoMovimiento);
        }

        if (fechaInicio != null && !fechaInicio.trim().isEmpty()) {
            sql.append(" AND m.fecha >= CAST(? AS DATE)");
            parametros.add(fechaInicio);
        }

        if (fechaFin != null && !fechaFin.trim().isEmpty()) {
            sql.append(" AND m.fecha < CAST(? AS DATE) + 1");
            parametros.add(fechaFin);
        }

        sql.append(" ORDER BY m.fecha DESC, m.id_movimiento DESC");

        return jdbcTemplate.query(sql.toString(), new MovimientoRowMapper(), parametros.toArray());
    }

}
