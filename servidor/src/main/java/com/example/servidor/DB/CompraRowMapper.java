package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.servidor.Model.Compra;



public class CompraRowMapper implements RowMapper<Compra> {

    @Override
    public Compra mapRow(ResultSet rs, int rowNum) throws SQLException {
       Compra compra = new Compra();
        compra.setIdCompra(rs.getInt("id_compra"));
        compra.setIdProveedor(rs.getString("id_proveedor"));
        compra.setIdUsuario(rs.getString("id_usuario"));
        compra.setFecha(rs.getString("fecha"));
        compra.setTotal(rs.getDouble("total"));
        compra.setNombreProveedor(rs.getString("nombre_proveedor"));
        return compra;
    }
    
}
