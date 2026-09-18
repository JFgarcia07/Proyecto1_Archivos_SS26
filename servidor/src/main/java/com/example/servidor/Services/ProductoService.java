package com.example.servidor.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.servidor.DB.ProductoDB;
import com.example.servidor.Model.Producto;

@Service 
public class ProductoService {
    
    private final ProductoDB productoDB;

    public ProductoService(ProductoDB productoDB) {
        this.productoDB = productoDB;
    }

    public List<Producto> listarProductos() {
        return productoDB.listarProductos();
    }

    public List<Producto> listarProductosInactivos(){
        return productoDB.listarProductosInactivos();
    }

    public Producto buscarPorId(String idProducto) {
        return productoDB.buscarProductoPorId(idProducto);
    }

    public String validarDatos(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().isEmpty()) {
            return "El nombre del producto no puede estar vacío.";
        }
        if (producto.getCategoria() == null || producto.getCategoria().isEmpty()) {
            return "La categoría del producto no puede estar vacía.";
        }
        if (producto.getPrecio() <= 0) {
            return "El precio del producto debe ser mayor a cero.";
        }
        return null; 
    }

     /**
     * Valida y crea un producto nuevo, generando su id automaticamente.
     *
     * @return el mensaje de error, o null si todo salio bien
     */
    public String crearProducto(Producto producto){
        String error = validarDatos(producto);
        if (error != null) {
            return error;
        }

        producto.setIdProducto(productoDB.generarSiguienteId());
        if (!productoDB.insertarProducto(producto)) {
            return "Error al crear el producto.";
        }
        return null;
    }

    /**
     * Valida y actualiza un producto existente.
     *
     * @return el mensaje de error, o null si todo salio bien
     */
    public String actualizarProductos(Producto producto){
        if(producto.getIdProducto() == null || producto.getIdProducto().isEmpty()){
            return "El id del producto no puede estar vacío.";
        }

        if (productoDB.buscarProductoPorId(producto.getIdProducto()) == null) {
            return "El producto con id " + producto.getIdProducto() + " no existe.";
        }

        String error = validarDatos(producto);
        if (error != null) {
            return error;
        }

        boolean actualizado = productoDB.actualizarProducto(producto);
        if (!actualizado) {
            return "Error al actualizar el producto.";
        }

        return null;
    }

    /**
     * Desactiva un producto, validando que exista primero.
     *
     * @return el mensaje de error, o null si todo salio bien
     */
    public String desactivarProducto(String idProducto) {
        Producto existente = productoDB.buscarProductoPorId(idProducto);

        if (existente == null) {
            return "El producto no existe";
        }

        if (!existente.isActivo()) {
            return "El producto ya se encuentra desactivado";
        }

        boolean exito = productoDB.desactivarProducto(idProducto);
        if (!exito) {
            return "No se pudo desactivar el producto";
        }
        return null;
    }
}
