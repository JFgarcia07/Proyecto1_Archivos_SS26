package com.example.servidor.DB;

import com.example.servidor.Model.DetalleVenta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Convierte una fila del ResultSet en un objeto DetalleVenta.
 *
 * @author jfgarcianata
 */
public class DetalleVentaRowMapper implements RowMapper<DetalleVenta> {

    @Override
    public DetalleVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdVenta(rs.getInt("id_venta"));
        detalle.setIdProducto(rs.getString("id_producto"));
        detalle.setCantidad(rs.getDouble("cantidad"));
        detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
        detalle.setNombreProducto(rs.getString("nombre_producto"));
        return detalle;
    }
}