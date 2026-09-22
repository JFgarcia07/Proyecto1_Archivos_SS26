package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.servidor.Model.ResumenPeriodo;

public class ResumenPeriodoRowMapper implements RowMapper<ResumenPeriodo>{
    @Override
    public ResumenPeriodo mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResumenPeriodo resumen = new ResumenPeriodo();
        resumen.setPeriodo(rs.getString("periodo"));
        resumen.setNumeroVentas(rs.getInt("numero_ventas"));
        resumen.setSubtotal(rs.getDouble("subtotal"));
        resumen.setIva(rs.getDouble("iva"));
        resumen.setTotal(rs.getDouble("total"));
        return resumen;
    }
}
