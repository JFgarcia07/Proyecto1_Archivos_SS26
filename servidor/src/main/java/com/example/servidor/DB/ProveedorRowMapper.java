package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.servidor.Model.Proveedor;

public class ProveedorRowMapper implements  RowMapper<Proveedor> {
    
    @Override
    public Proveedor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(rs.getString("id_proveedor"));
        proveedor.setNombreProveedor(rs.getString("nombre_proveedor"));
        proveedor.setUbicacion(rs.getString("ubicacion"));
        proveedor.setActivo(rs.getBoolean("activo"));
        return proveedor;
    }
}
