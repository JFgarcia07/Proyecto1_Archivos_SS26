package com.example.servidor.DB;

import com.example.servidor.Model.LogSistema;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LogRowMapper implements RowMapper<LogSistema>{
    
    @Override
    public LogSistema mapRow(ResultSet rs, int rowNum) throws SQLException {
        LogSistema log = new LogSistema();
        log.setIdLog(rs.getInt("id_log"));
        log.setIdUsuario(rs.getString("id_usuario"));
        log.setNombreUsuario(rs.getString("nombre_usuario"));
        log.setAccion(rs.getString("accion"));
        log.setModulo(rs.getString("modulo"));
        log.setFecha(rs.getString("fecha"));
        log.setDetalle(rs.getString("detalle"));
        return log;
    }
}
