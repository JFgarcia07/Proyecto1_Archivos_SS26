package com.example.servidor.DB;

import com.example.servidor.Model.Venta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class VentaRowMapper implements RowMapper<Venta> {

    @Override
    public Venta mapRow(ResultSet rs, int rowNum) throws SQLException {
        Venta venta = new Venta();
        venta.setIdVenta(rs.getInt("id_venta"));
        venta.setIdCliente(rs.getInt("id_cliente"));
        venta.setIdUsuario(rs.getString("id_usuario"));
        venta.setFecha(rs.getString("fecha"));
        venta.setSubtotal(rs.getDouble("subtotal"));
        venta.setIva(rs.getDouble("iva"));
        venta.setTotal(rs.getDouble("total"));
        venta.setNombreCliente(rs.getString("nombre"));
        venta.setNitCliente(rs.getString("nit"));
        venta.setNombreUsuario(rs.getString("nombre_usuario"));
        return venta;
    }
}