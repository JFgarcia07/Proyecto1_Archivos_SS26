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

import com.example.servidor.Model.Proveedor;
import com.example.servidor.Services.ProveedorService;

@RestController 
@RequestMapping("/api/proveedores")
public class ProveedorController {
    
    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> listar() {
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Proveedor>> listarInactivos() {
        return ResponseEntity.ok(proveedorService.listarProveedoresInactivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable String id) {
        Proveedor proveedor = proveedorService.buscarPorId(id);

        if (proveedor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody Proveedor proveedor) {
        String error = proveedorService.crearProveedor(proveedor);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok("Proveedor creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable String id, @RequestBody Proveedor proveedor) {
        proveedor.setIdProveedor(id);
        String error = proveedorService.actualizarProveedor(proveedor);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok("Proveedor actualizado correctamente");
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivar(@PathVariable String id) {
        String error = proveedorService.desactivarProveedor(id);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok("Proveedor desactivado correctamente");
    }
}
