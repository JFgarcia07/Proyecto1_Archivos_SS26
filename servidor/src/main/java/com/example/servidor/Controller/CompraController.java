package com.example.servidor.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.servidor.Model.Compra;
import com.example.servidor.Services.CompraService;
import com.example.servidor.Services.LogService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {
    
    private final CompraService compraService;
    private final LogService logService;

    public CompraController(CompraService compraService, LogService logService) {
        this.compraService = compraService;
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listar() {
        return ResponseEntity.ok(compraService.listarCompras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> buscarPorId(@PathVariable Integer id) {
        Compra compra = compraService.buscarPorId(id);

        if (compra == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(compra);
    }

    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody Compra compra) {
        String error = compraService.registrarCompra(compra);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        logService.registrar(LogService.ACCION_REGISTRAR, LogService.MODULO_COMPRAS,
            "Registro la compra No. " + compra.getIdCompra() +
            " al proveedor " + compra.getIdProveedor() +
            " por " + logService.formatearMonto(compra.getTotal()));
        return ResponseEntity.ok("Compra registrada correctamente");
    }
}
