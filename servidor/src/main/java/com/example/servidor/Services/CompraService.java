package com.example.servidor.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.servidor.DB.CompraDB;
import com.example.servidor.DB.ProductoDB;
import com.example.servidor.DB.ProveedorDB;
import com.example.servidor.Model.Compra;
import com.example.servidor.Model.DetalleCompra;
import com.example.servidor.Model.Producto;
import com.example.servidor.Model.Proveedor;

import jakarta.transaction.Transactional;

@Service 
public class CompraService {
    private final CompraDB compraDB;
    private final ProveedorDB proveedorDB;
    private final ProductoDB productoDB;

    public CompraService(CompraDB compraDB, ProveedorDB proveedorDB, ProductoDB productoDB) {
        this.compraDB = compraDB;
        this.proveedorDB = proveedorDB;
        this.productoDB = productoDB;
    }

    public List<Compra> listarCompras() {
        return compraDB.listarCompras();
    }

    /**
     * Devuelve una compra con sus lineas de detalle cargadas.
     */
    public Compra buscarPorId(Integer idCompra) {
        Compra compra = compraDB.buscarPorId(idCompra);

        if (compra == null) {
            return null;
        }

        List<DetalleCompra> detalles = compraDB.listarDetalles(idCompra);
        compra.setDetalles(detalles);
        return compra;
    }

    /**
     * Registra una compra completa de forma transaccional.
     *
     * @return el mensaje de error, o null si todo salio bien
     */
    @Transactional
    public String registrarCompra(Compra compra) {
        String error = validarCompra(compra);
        if (error != null) {
            return error;
        }

        double total = 0;
        for (int i = 0; i < compra.getDetalles().size(); i++) {
            DetalleCompra detalle = compra.getDetalles().get(i);
            total = total + (detalle.getCantidad() * detalle.getPrecioUnitario());
        }
        compra.setTotal(total);

        Integer idCompra = compraDB.insertarCompra(compra);

        if (idCompra == null) {
            return "No se pudo registrar la compra";
        }

        for (int i = 0; i < compra.getDetalles().size(); i++) {
            DetalleCompra detalle = compra.getDetalles().get(i);

            compraDB.insertarDetalle(idCompra, detalle);
            compraDB.insertarMovimientoEntrada(detalle.getIdProducto(), detalle.getCantidad(), idCompra);
            compraDB.aumentarStock(detalle.getIdProducto(), detalle.getCantidad());
        }

        return null;
    }

    /**
     * Valida que la compra tenga proveedor, usuario y al menos una
     * linea de detalle con datos correctos.
     *
     * @return el mensaje de error, o null si los datos son validos
     */
    private String validarCompra(Compra compra) {
        if (compra.getIdProveedor() == null || compra.getIdProveedor().trim().isEmpty()) {
            return "Debe seleccionar un proveedor";
        }

        Proveedor proveedor = proveedorDB.buscarPorId(compra.getIdProveedor());
        if (proveedor == null) {
            return "El proveedor seleccionado no existe";
        }

        if (compra.getIdUsuario() == null || compra.getIdUsuario().trim().isEmpty()) {
            return "No se identifico al usuario que registra la compra";
        }

        if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
            return "La compra debe incluir al menos un producto";
        }

        for (int i = 0; i < compra.getDetalles().size(); i++) {
            DetalleCompra detalle = compra.getDetalles().get(i);

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
        }

        return null;
    }
}
