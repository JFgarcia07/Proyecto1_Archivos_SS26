package com.example.servidor.DB;

import com.example.servidor.Model.LogSistema;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LogDB {
    
    private final JdbcTemplate jdbcTemplate;

    public LogDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String QUERY_INSERTAR_LOG =
        "INSERT INTO LogSistema (id_usuario, accion, modulo, fecha, detalle) " +
        "VALUES (?, ?, ?, now(), ?)";
    public void insertarLog(String idUsuario, String accion, String modulo, String detalle) {
        jdbcTemplate.update(QUERY_INSERTAR_LOG, idUsuario, accion, modulo, detalle);
    }

    private final String QUERY_LISTAR_BASE =
        "SELECT l.*, u.nombre_usuario " +
        "FROM LogSistema l " +
        "INNER JOIN Usuario u ON l.id_usuario = u.id_usuario " +
        "WHERE 1 = 1";
     /**
     * Lista los logs aplicando solo los filtros que vengan con valor.
     * Cada filtro presente agrega su condicion y su parametro.
     *
     * @param modulo      modulo a filtrar, o null/vacio para todos
     * @param fechaInicio fecha inicial (yyyy-MM-dd), o null/vacio
     * @param fechaFin    fecha final inclusive (yyyy-MM-dd), o null/vacio
     */
    public List<LogSistema> listarLogs(String modulo, String fechaInicio, String fechaFin) {
        StringBuilder sql = new StringBuilder(QUERY_LISTAR_BASE);
        List<Object> parametros = new ArrayList<>();

        if (modulo != null && !modulo.trim().isEmpty()) {
            sql.append(" AND l.modulo = ?");
            parametros.add(modulo);
        }

        if (fechaInicio != null && !fechaInicio.trim().isEmpty()) {
            sql.append(" AND l.fecha >= CAST(? AS DATE)");
            parametros.add(fechaInicio);
        }

        if (fechaFin != null && !fechaFin.trim().isEmpty()) {
            sql.append(" AND l.fecha < CAST(? AS DATE) + 1");
            parametros.add(fechaFin);
        }

        sql.append(" ORDER BY l.fecha DESC");

        return jdbcTemplate.query(sql.toString(), new LogRowMapper(), parametros.toArray());
    }
}
