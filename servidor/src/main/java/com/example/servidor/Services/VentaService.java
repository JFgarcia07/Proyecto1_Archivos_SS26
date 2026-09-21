package com.example.servidor.Services;

import com.example.servidor.DB.ClienteDB;
import com.example.servidor.DB.ProductoDB;
import com.example.servidor.DB.VentaDB;
import com.example.servidor.Model.Cliente;
import com.example.servidor.Model.DetalleVenta;
import com.example.servidor.Model.Producto;
import com.example.servidor.Model.Venta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VentaService {
    
    private static final double PORCENTAJE_IVA = 0.12;

    private final VentaDB ventaDB;
    private final ClienteDB clienteDB;
    private final ProductoDB productoDB;

    public VentaService(VentaDB ventaDB, ClienteDB clienteDB, ProductoDB productoDB) {
        this.ventaDB = ventaDB;
        this.clienteDB = clienteDB;
        this.productoDB = productoDB;
    }

    public List<Venta> listarVentas() {
        return ventaDB.listarVentas();
    }

    /**
     * Devuelve una venta con sus lineas de detalle cargadas.
     */
    public Venta buscarPorId(Integer idVenta) {
        Venta venta = ventaDB.buscarPorId(idVenta);

        if (venta == null) {
            return null;
        }

        List<DetalleVenta> detalles = ventaDB.listarDetalles(idVenta);
        venta.setDetalles(detalles);
        return venta;
    }

    /**
     * Registra una venta completa de forma transaccional, validando
     * previamente que haya existencias suficientes.
     *
     * @return el mensaje de error, o null si todo salio bien
     */
    @Transactional
    public String registrarVenta(Venta venta) {
        String error = validarVenta(venta);
        if (error != null) {
            return error;
        }

        double subtotal = 0;
        for (int i = 0; i < venta.getDetalles().size(); i++) {
            DetalleVenta detalle = venta.getDetalles().get(i);
            subtotal = subtotal + (detalle.getCantidad() * detalle.getPrecioUnitario());
        }

        double iva = subtotal * PORCENTAJE_IVA;
        double total = subtotal + iva;

        venta.setSubtotal(redondear(subtotal));
        venta.setIva(redondear(iva));
        venta.setTotal(redondear(total));

        Integer idVenta = ventaDB.insertarVenta(venta);

        if (idVenta == null) {
            return "No se pudo registrar la venta";
        }

        for (int i = 0; i < venta.getDetalles().size(); i++) {
            DetalleVenta detalle = venta.getDetalles().get(i);

            ventaDB.insertarDetalle(idVenta, detalle);
            ventaDB.insertarMovimientoSalida(detalle.getIdProducto(), detalle.getCantidad(), idVenta);
            ventaDB.disminuirStock(detalle.getIdProducto(), detalle.getCantidad());
        }

        venta.setIdVenta(idVenta);
        return null;
    }

    /**
     * Valida cliente, usuario, lineas de detalle y, lo mas importante,
     * que exista suficiente stock de cada producto antes de vender.
     *
     * @return el mensaje de error, o null si los datos son validos
     */
    private String validarVenta(Venta venta) {
        if (venta.getIdCliente() == null) {
            return "Debe seleccionar un cliente";
        }

        Cliente cliente = clienteDB.buscarPorId(venta.getIdCliente());
        if (cliente == null) {
            return "El cliente seleccionado no existe";
        }

        if (venta.getIdUsuario() == null || venta.getIdUsuario().trim().isEmpty()) {
            return "No se identifico al usuario que registra la venta";
        }

        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            return "La venta debe incluir al menos un producto";
        }

        for (int i = 0; i < venta.getDetalles().size(); i++) {
            DetalleVenta detalle = venta.getDetalles().get(i);

            if (detalle.getIdProducto() == null || detalle.getIdProducto().trim().isEmpty()) {
                return "Todas las lineas deben tener un producto seleccionado";
            }

            Producto producto = productoDB.buscarProductoPorId(detalle.getIdProducto());
            if (producto == null) {
                return "El producto " + detalle.getIdProducto() + " no existe";
            }

            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                return "La cantidad debe ser mayor a cero";
            }

            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario() < 0) {
                return "El precio unitario no puede ser negativo";
            }

            if (producto.getStock() < detalle.getCantidad()) {
                return "Existencia insuficiente de " + producto.getNombreProducto() +
                       ". Disponible: " + producto.getStock() +
                       ", solicitado: " + detalle.getCantidad();
            }
        }

        return null;
    }

    /**
     * Redondea un valor monetario a dos decimales.
     */
    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
