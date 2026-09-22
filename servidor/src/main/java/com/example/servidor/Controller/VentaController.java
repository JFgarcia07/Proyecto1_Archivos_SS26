package com.example.servidor.Controller;

import com.example.servidor.Model.Venta;
import com.example.servidor.Services.FacturaService;
import com.example.servidor.Services.VentaService;
import com.example.servidor.Services.LogService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    
    private final VentaService ventaService;
    private final FacturaService facturaService;
    private final LogService logService;

    public VentaController(VentaService ventaService, FacturaService facturaService, LogService logService) {
        this.ventaService = ventaService;
        this.facturaService = facturaService;
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Integer id) {
        Venta venta = ventaService.buscarPorId(id);

        if (venta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venta);
    }

    /**
     * Registra la venta y devuelve el id generado, para que el
     * cliente pueda solicitar la factura inmediatamente.
     */
    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody Venta venta) {
        String error = ventaService.registrarVenta(venta);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        logService.registrar(LogService.ACCION_REGISTRAR, LogService.MODULO_VENTAS,
            "Registro la venta No. " + venta.getIdVenta() +
            " al cliente " + venta.getIdCliente() +
            " por " + logService.formatearMonto(venta.getTotal()));
        return ResponseEntity.ok(String.valueOf(venta.getIdVenta()));
    }

    /**
     * Genera y devuelve la factura PDF de una venta.
     */
    @GetMapping("/{id}/factura")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable Integer id) {
        byte[] pdf = facturaService.generarFactura(id);

        if (pdf == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "factura_" + id + ".pdf");

        logService.registrar(LogService.ACCION_GENERAR_FACTURA, LogService.MODULO_VENTAS,
            "Genero la factura de la venta No. " + id);
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
