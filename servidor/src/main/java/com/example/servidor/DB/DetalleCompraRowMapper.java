package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.servidor.Model.DetalleCompra;

public class DetalleCompraRowMapper implements RowMapper<DetalleCompra> {

    @Override
    public DetalleCompra mapRow(ResultSet rs, int rowNum) throws SQLException {
        DetalleCompra detalleComrpa = new DetalleCompra();
        detalleComrpa.setIdCompra(rs.getInt("id_compra"));
        detalleComrpa.setIdProducto(rs.getString("id_producto"));
        detalleComrpa.setCantidad(rs.getDouble("cantidad"));
        detalleComrpa.setPrecioUnitario(rs.getDouble("precio_unitario"));
        detalleComrpa.setNombreProducto(rs.getString("nombre_producto"));
        return detalleComrpa;
    }
}
