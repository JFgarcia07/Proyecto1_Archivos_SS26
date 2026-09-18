package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.servidor.Model.Producto;

public class ProductoRowMapper implements RowMapper<Producto> {
    @Override
    public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getString("id_producto"));
        producto.setNombreProducto(rs.getString("nombre_producto"));
        producto.setCategoria(rs.getString("categoria"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setActivo(rs.getBoolean("activo"));
        return producto;
    }
    
}
