package com.example.servidor.DB;

import com.example.servidor.Model.MovimientoInventario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovimientoRowMapper implements RowMapper<MovimientoInventario> {
    
    @Override
    public MovimientoInventario mapRow(ResultSet rs, int rowNum) throws SQLException {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdMovimiento(rs.getInt("id_movimiento"));
        movimiento.setIdProducto(rs.getString("id_producto"));
        movimiento.setNombreProducto(rs.getString("nombre_producto"));
        movimiento.setTipoMovimiento(rs.getString("tipo_movimiento"));
        movimiento.setCantidad(rs.getDouble("cantidad"));
        movimiento.setFecha(rs.getString("fecha"));
        movimiento.setOrigen(rs.getString("origen"));
        movimiento.setIdReferencia(rs.getInt("id_referencia"));
        movimiento.setTercero(rs.getString("tercero"));
        movimiento.setIdUsuario(rs.getString("id_usuario"));
        return movimiento;
    }
}
