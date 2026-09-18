package com.example.servidor.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.servidor.Model.Producto;
import com.example.servidor.Services.ProductoService;

@RestController 
@RequestMapping ("/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping 
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.listarProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Producto>> listarProductosInactivos() {
        List<Producto> productosInactivos = productoService.listarProductosInactivos();
        return ResponseEntity.ok(productosInactivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable String id) {
        Producto producto = productoService.buscarPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody Producto producto) {
        String error = productoService.crearProducto(producto);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok("Producto creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable String id, @RequestBody Producto producto) {
        producto.setIdProducto(id);
        String error = productoService.actualizarProductos(producto);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok("Producto actualizado correctamente");
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivar(@PathVariable String id) {
        String error = productoService.desactivarProducto(id);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok("Producto desactivado correctamente");
    }
}
