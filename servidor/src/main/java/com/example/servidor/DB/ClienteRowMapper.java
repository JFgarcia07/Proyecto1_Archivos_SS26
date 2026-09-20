package com.example.servidor.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.servidor.Model.Cliente;



public class ClienteRowMapper implements RowMapper<Cliente> {

    @Override
    public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setNit(rs.getString("nit"));
        cliente.setSaldo(rs.getDouble("saldo"));
        cliente.setActivo(rs.getBoolean("activo"));
        return cliente;
    }
    
}
