package com.example.servidor.DB;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.servidor.Model.Producto;

@Repository
public class ProductoDB {
    
    private final JdbcTemplate jdbcTemplate;

    public ProductoDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
    * Devuelve todos los productos activos, ordenados por id.
    */
    private final String QUERY_LISTAR_PRODUCTOS = "SELECT * FROM producto WHERE activo = TRUE ORDER BY id_producto";
    public List<Producto> listarProductos(){
        return jdbcTemplate.query(QUERY_LISTAR_PRODUCTOS, new ProductoRowMapper());
    }

    /**
    * Devuelve todos los productos activos, ordenados por id.
    */
    private final String QUERY_LISTAR_PRODUCTOS_INACTIVOS = "SELECT * FROM producto WHERE activo = FALSE ORDER BY id_producto";
    public List<Producto> listarProductosInactivos(){
        return jdbcTemplate.query(QUERY_LISTAR_PRODUCTOS_INACTIVOS, new ProductoRowMapper());
    }

    /**
     * Busca un producto por su id.
     *
     * @return el Producto encontrado, o null si no existe
     */
    private final String QUERY_BUSCAR_PRODUCTO_POR_ID = "SELECT * FROM producto WHERE id_producto = ?";
    public Producto buscarProductoPorId(String idProducto) {
        List<Producto> resultado = jdbcTemplate.query(QUERY_BUSCAR_PRODUCTO_POR_ID, new ProductoRowMapper(), idProducto);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    /**
     * Inserta un producto nuevo. El stock siempre inicia en 0:
     * solo las compras pueden aumentarlo.
     *
     * @return true si se inserto correctamente
     */
    private final String QUERY_INSERTAR_PRODUCTO = "INSERT INTO Producto (id_producto, nombre_producto, categoria, precio, stock, activo) VALUES (?, ?, ?, ?, 0, TRUE)";
    public boolean insertarProducto(Producto producto) {
        int rowsAffected = jdbcTemplate.update(QUERY_INSERTAR_PRODUCTO, producto.getIdProducto(), producto.getNombreProducto(), producto.getCategoria(), producto.getPrecio());
        return rowsAffected > 0;
    }

    /**
     * Actualiza un producto existente.
     *
     * @return true si se actualizo correctamente
     */
    private final String QUERY_ACTUALIZAR_PRODUCTO = "UPDATE Producto SET nombre_producto = ?, categoria = ?, precio = ? WHERE id_producto = ?";
    public boolean actualizarProducto(Producto producto) {
        int rowsAffected = jdbcTemplate.update(QUERY_ACTUALIZAR_PRODUCTO, producto.getNombreProducto(), producto.getCategoria(), producto.getPrecio(), producto.getIdProducto());
        return rowsAffected > 0;
    }

    /**
     * Genera el siguiente id disponible con formato PR001, PR002, etc.
     */
    public String generarSiguienteId() {
        String sql = "SELECT MAX(id_producto) FROM Producto";
        String maximo = jdbcTemplate.queryForObject(sql, String.class);

        if (maximo == null) {
            return "PR001";
        }

        int numero = Integer.parseInt(maximo.substring(2));
        numero = numero + 1;
        return String.format("PR%03d", numero);
    }

     /**
     * Desactiva un producto (baja logica). No se elimina fisicamente
     * para conservar el historial de compras y ventas que lo referencian.
     *
     * @return true si se desactivo correctamente
     */
    public boolean desactivarProducto(String idProducto) {
        String sql = "UPDATE Producto SET activo = FALSE WHERE id_producto = ?";
        int filas = jdbcTemplate.update(sql, idProducto);
        return filas > 0;
    }
}
