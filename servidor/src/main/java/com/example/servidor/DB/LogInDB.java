package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.servidor.Model.Usuario;

/**
 * Acceso a datos para el login: busca al usuario por su email.
 * No compara el password aqui, porque esta cifrado con BCrypt
 * y esa comparacion debe hacerse en Java, no en SQL.
 *
 * @author jfgarcianata
 */
@Repository
public class LogInDB {
    private final JdbcTemplate jdbcTemplate;

    public LogInDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        List<Usuario> resultado = jdbcTemplate.query(sql, new RowMapper<Usuario>() {
            @Override
            public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getString("id_usuario"));
                u.setNombre(rs.getString("nombre_usuario"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setActivo(rs.getBoolean("activo"));
                return u;
            }
        }, email);

        if (resultado.isEmpty()) {
            return null;
        }
        return resultado.get(0);
    }
}
