package com.example.servidor.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.servidor.Model.MovimientoInventario;
import com.example.servidor.Model.Producto;
import com.example.servidor.Services.InventarioService;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/existencias")
    public ResponseEntity<List<Producto>> existencias() {
        return ResponseEntity.ok(inventarioService.listarExistencias());
    }

    @GetMapping("/alertas")
    public ResponseEntity<List<Producto>> alertas() {
        return ResponseEntity.ok(inventarioService.listarAlertas());
    }

    @GetMapping("/stock-minimo")
    public ResponseEntity<Double> stockMinimo() {
        return ResponseEntity.ok(inventarioService.getStockMinimo());
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoInventario>> movimientos(
            @RequestParam(name = "idProducto", required = false) String idProducto,
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
            inventarioService.listarMovimientos(idProducto, tipo, fechaInicio, fechaFin)
        );
    }
}
