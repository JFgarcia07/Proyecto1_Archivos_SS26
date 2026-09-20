package com.example.servidor.DB;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.servidor.Model.Cliente;

@Repository 
public class ClienteDB {
    
    private final JdbcTemplate jdbcTemplate;

     public ClienteDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String QUERY_LISTAR_CLIENTES = "SELECT * FROM Cliente WHERE activo = TRUE ORDER BY id_cliente";
    public List<Cliente> listarClientes(){
        return jdbcTemplate.query(QUERY_LISTAR_CLIENTES, new ClienteRowMapper());
    }

    private final String QUERY_LISTAR_CLIENTES_INACTIVOS = "SELECT * FROM Cliente WHERE activo = FALSE ORDER BY id_cliente"; 
    public List<Cliente> listarClientesInactivos(){
        return jdbcTemplate.query(QUERY_LISTAR_CLIENTES_INACTIVOS, new ClienteRowMapper());
    }

    private final String QUERY_BUSCAR_POR_ID = "SELECT * FROM Cliente WHERE id_cliente = ?";
    public Cliente buscarPorId(Integer idCliente){
        List<Cliente> resultado = jdbcTemplate.query(QUERY_BUSCAR_POR_ID, new ClienteRowMapper(), idCliente);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    /**
     * Verifica si ya existe otro cliente con el mismo NIT.
     * Se usa para no violar la restriccion UNIQUE de la tabla.
     */
    public boolean existeNit(String nit, Integer idClienteExcluir) {
        String sql;
        Integer cantidad;

        if (idClienteExcluir == null) {
            sql = "SELECT COUNT(*) FROM Cliente WHERE nit = ?";
            cantidad = jdbcTemplate.queryForObject(sql, Integer.class, nit);
        } else {
            sql = "SELECT COUNT(*) FROM Cliente WHERE nit = ? AND id_cliente <> ?";
            cantidad = jdbcTemplate.queryForObject(sql, Integer.class, nit, idClienteExcluir);
        }

        if (cantidad == null) {
            return false;
        }
        return cantidad > 0;
    }

    private final String QUERY_INSERTAR_CLIENTE = "INSERT INTO Cliente (nombre, nit, saldo, activo) VALUES (?, ?, 0, TRUE)";
    public boolean insertarCliente(Cliente cliente){
        int filas = jdbcTemplate.update(QUERY_INSERTAR_CLIENTE,
                cliente.getNombre(),
                cliente.getNit());

        return filas > 0;
    }

    private final String QUERY_ACTUALIZAR_CLIENTE = "UPDATE Cliente SET nombre = ?, nit = ? WHERE id_cliente = ?";
    public boolean actualizarCliente(Cliente cliente){
        int filas = jdbcTemplate.update(QUERY_ACTUALIZAR_CLIENTE,
                cliente.getNombre(),
                cliente.getNit(),
                cliente.getIdCliente());

        return filas > 0;
    }

    private final String QUERY_DESACTIVAR_CLIENTE = "UPDATE Cliente SET activo = FALSE WHERE id_cliente = ?";
    public boolean desactivarCliente(Integer idCliente){
        int filas = jdbcTemplate.update(QUERY_DESACTIVAR_CLIENTE, idCliente);
        return filas > 0;
    }
}