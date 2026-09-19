package com.example.servidor.DB;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.servidor.Model.Proveedor;

@Repository 
public class ProveedorDB {
    private final JdbcTemplate jdbcTemplate;

    public ProveedorDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String QUERY_LISTAR_PROVEEDORES = "SELECT * FROM Proveedor WHERE activo = TRUE ORDER BY id_proveedor";
    public List<Proveedor> listarProveedores(){
        return jdbcTemplate.query(QUERY_LISTAR_PROVEEDORES, new ProveedorRowMapper());
    }

    private final String QUERY_LISTAR_PROVEEDORES_INACTIVOS = "SELECT * FROM Proveedor WHERE activo = FALSE ORDER BY id_proveedor";
    public List<Proveedor> listarProveedoresInactivos(){
        return jdbcTemplate.query(QUERY_LISTAR_PROVEEDORES_INACTIVOS, new ProveedorRowMapper());
    }

    private final String QUERY_BUSCAR_PROVEEDOR_POR_ID = "SELECT * FROM Proveedor WHERE id_proveedor = ?";
    public Proveedor buscarPorId(String idProveedor){
        List<Proveedor> resultado = jdbcTemplate.query(QUERY_BUSCAR_PROVEEDOR_POR_ID, new ProveedorRowMapper());
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    private final String QUERY_INSERTAR_PROVEEDOR = "INSERT INTO Proveedor (id_proveedor, nombre_proveedor, ubicacion, activo) VALUES (?, ?, ?, TRUE)";
    public boolean insertarProveedor(Proveedor proveedor){
        int filas = jdbcTemplate.update(QUERY_INSERTAR_PROVEEDOR, proveedor.getIdProveedor(), 
            proveedor.getNombreProveedor(),
            proveedor.getUbicacion()
        );

        return filas > 0;
    }

    private final String QUERY_ACTUALIZAR_PROVEEDOR = "UPDATE Proveedor SET nombre_proveedor = ?, ubicacion = ? WHERE id_proveedor = ?";
    public boolean actualizarProveedor(Proveedor proveedor){
        int filas = jdbcTemplate.update(QUERY_ACTUALIZAR_PROVEEDOR,
                proveedor.getNombreProveedor(),
                proveedor.getUbicacion(),
                proveedor.getIdProveedor());

        return filas > 0;
    }

    private final String QUERY_DESACTIVAR_PROVEEDOR = "UPDATE Proveedor SET activo = FALSE WHERE id_proveedor = ?";
    public boolean desactivarProveedor(String idProveedor){
        int filas = jdbcTemplate.update(QUERY_DESACTIVAR_PROVEEDOR, idProveedor);
        return filas > 0; 
    }

    /**
     * Genera el siguiente id disponible con formato PV001, PV002, etc.
     */
    public String generarSiguienteId() {
        String sql = "SELECT MAX(id_proveedor) FROM Proveedor";
        String maximo = jdbcTemplate.queryForObject(sql, String.class);

        if (maximo == null) {
            return "PV001";
        }

        int numero = Integer.parseInt(maximo.substring(2));
        numero = numero + 1;
        return String.format("PV%03d", numero);
    }
}
