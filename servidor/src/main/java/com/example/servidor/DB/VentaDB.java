package com.example.servidor.DB;

import com.example.servidor.Model.DetalleVenta;
import com.example.servidor.Model.Venta;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public class VentaDB {
    private final JdbcTemplate jdbcTemplate;

    public VentaDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String QUERY_LISTAR_VENTAS =
        "SELECT v.*, c.nombre, c.nit " +
        "FROM Venta v " +
        "INNER JOIN Cliente c ON v.id_cliente = c.id_cliente " +
        "ORDER BY v.id_venta DESC";
    public List<Venta> listarVentas(){
        return jdbcTemplate.query(QUERY_LISTAR_VENTAS, new VentaRowMapper());
    }

    private final String QUERY_BUSCAR_VENTA_POR_ID =
        "SELECT v.*, c.nombre, c.nit " +
        "FROM Venta v " +
        "INNER JOIN Cliente c ON v.id_cliente = c.id_cliente " +
        "WHERE v.id_venta = ?";
    public Venta buscarPorId(Integer idVenta) {
        List<Venta> resultado = jdbcTemplate.query(
            QUERY_BUSCAR_VENTA_POR_ID,
            new VentaRowMapper(),
            idVenta
        );
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    private final String QUERY_DETALLES_DE_VENTA =
        "SELECT dv.*, pr.nombre_producto " +
        "FROM DetalleVenta dv " +
        "INNER JOIN Producto pr ON dv.id_producto = pr.id_producto " +
        "WHERE dv.id_venta = ?";
    public List<DetalleVenta> listarDetalles(Integer idVenta) {
        return jdbcTemplate.query(
            QUERY_DETALLES_DE_VENTA,
            new DetalleVentaRowMapper(),
            idVenta
        );
    }

    private final String QUERY_INSERTAR_VENTA =
        "INSERT INTO Venta (id_cliente, id_usuario, fecha, subtotal, iva, total) " +
        "VALUES (?, ?, now(), ?, ?, ?) RETURNING id_venta";
    /**
     * Inserta la cabecera de la venta y devuelve el id generado
     * por PostgreSQL, gracias a la clausula RETURNING.
     */
    public Integer insertarVenta(Venta venta) {
        return jdbcTemplate.queryForObject(
            QUERY_INSERTAR_VENTA,
            Integer.class,
            venta.getIdCliente(),
            venta.getIdUsuario(),
            venta.getSubtotal(),
            venta.getIva(),
            venta.getTotal()
        );
    }

    private final String QUERY_INSERTAR_DETALLE =
        "INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario) " +
        "VALUES (?, ?, ?, ?)";
    public void insertarDetalle(Integer idVenta, DetalleVenta detalle) {
        jdbcTemplate.update(
            QUERY_INSERTAR_DETALLE,
            idVenta,
            detalle.getIdProducto(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario()
        );
    }

    private final String QUERY_INSERTAR_MOVIMIENTO =
        "INSERT INTO MovimientoInventario (id_producto, tipo_movimiento, cantidad, fecha, origen, id_referencia) " +
        "VALUES (?, 'SALIDA', ?, now(), 'VENTA', ?)";
    public void insertarMovimientoSalida(String idProducto, Double cantidad, Integer idVenta) {
        jdbcTemplate.update(
            QUERY_INSERTAR_MOVIMIENTO,
            idProducto,
            cantidad,
            idVenta
        );
    }

    private final String QUERY_DISMINUIR_STOCK = "UPDATE Producto SET stock = stock - ? WHERE id_producto = ?";
    public void disminuirStock(String idProducto, Double cantidad) {
        jdbcTemplate.update(QUERY_DISMINUIR_STOCK, cantidad, idProducto);
    }
}
