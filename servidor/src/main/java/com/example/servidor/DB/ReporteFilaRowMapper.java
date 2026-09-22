package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.servidor.Model.ReporteFila;

public class ReporteFilaRowMapper implements RowMapper<ReporteFila> {
    @Override
    public ReporteFila mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReporteFila fila = new ReporteFila();
        fila.setCodigo(rs.getString("codigo"));
        fila.setNombre(rs.getString("nombre"));
        fila.setDetalle(rs.getString("detalle"));
        fila.setCantidad(rs.getDouble("cantidad"));
        fila.setConteo(rs.getInt("conteo"));
        fila.setMonto(rs.getDouble("monto"));
        return fila;
    }
}
