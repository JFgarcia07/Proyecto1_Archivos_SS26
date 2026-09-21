package com.example.servidor.DB;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.servidor.Model.Compra;
import com.example.servidor.Model.DetalleCompra;

@Repository 
public class CompraDB {

    private final JdbcTemplate jdbcTemplate;

    public CompraDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String QUERY_LISTAR_COMPRAS =
        "SELECT c.*, p.nombre_proveedor " +
        "FROM Compra c " +
        "INNER JOIN Proveedor p ON c.id_proveedor = p.id_proveedor " +
        "ORDER BY c.id_compra DESC";
    public List<Compra> listarCompras() {
        return jdbcTemplate.query(QUERY_LISTAR_COMPRAS, new CompraRowMapper());
    }

    private final String QUERY_BUSCAR_COMPRA_POR_ID =
        "SELECT c.*, p.nombre_proveedor " +
        "FROM Compra c " +
        "INNER JOIN Proveedor p ON c.id_proveedor = p.id_proveedor " +
        "WHERE c.id_compra = ?";
    public Compra buscarPorId(Integer idCompra) {
        List<Compra> resultado = jdbcTemplate.query(
            QUERY_BUSCAR_COMPRA_POR_ID,
            new CompraRowMapper(),
            idCompra
        );
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    private final String QUERY_DETALLES_DE_COMPRA =
        "SELECT dc.*, pr.nombre_producto " +
        "FROM DetalleCompra dc " +
        "INNER JOIN Producto pr ON dc.id_producto = pr.id_producto " +
        "WHERE dc.id_compra = ?";
    public List<DetalleCompra> listarDetalles(Integer idCompra) {
        return jdbcTemplate.query(
            QUERY_DETALLES_DE_COMPRA,
            new DetalleCompraRowMapper(),
            idCompra
        );
    }

    private final String QUERY_INSERTAR_COMPRA =
        "INSERT INTO Compra (id_proveedor, id_usuario, fecha, total) " +
        "VALUES (?, ?, now(), ?) RETURNING id_compra";
    /**
     * Inserta la cabecera de la compra y devuelve el id generado
     * por PostgreSQL, gracias a la clausula RETURNING.
     */
    public Integer insertarCompra(Compra compra) {
        return jdbcTemplate.queryForObject(
            QUERY_INSERTAR_COMPRA,
            Integer.class,
            compra.getIdProveedor(),
            compra.getIdUsuario(),
            compra.getTotal()
        );
    }

    private final String QUERY_INSERTAR_DETALLE =
        "INSERT INTO DetalleCompra (id_compra, id_producto, cantidad, precio_unitario) " +
        "VALUES (?, ?, ?, ?)";
    public void insertarDetalle(Integer idCompra, DetalleCompra detalle) {
        jdbcTemplate.update(
            QUERY_INSERTAR_DETALLE,
            idCompra,
            detalle.getIdProducto(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario()
        );
    }

    private final String QUERY_INSERTAR_MOVIMIENTO =
        "INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) " +
        "VALUES (?, 'ENTRADA', ?, now(), 'COMPRA', ?)";
    public void insertarMovimientoEntrada(String idProducto, Double cantidad, Integer idCompra) {
        jdbcTemplate.update(
            QUERY_INSERTAR_MOVIMIENTO,
            idProducto,
            cantidad,
            idCompra
        );
    }

    private final String QUERY_AUMENTAR_STOCK =
        "UPDATE Producto SET stock = stock + ? WHERE id_producto = ?";
    public void aumentarStock(String idProducto, Double cantidad) {
        jdbcTemplate.update(QUERY_AUMENTAR_STOCK, cantidad, idProducto);
    }
}
