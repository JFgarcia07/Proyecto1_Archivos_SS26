package com.example.servidor.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.servidor.Model.Compra;
import com.example.servidor.Model.ReporteFila;
import com.example.servidor.Model.ResumenPeriodo;
import com.example.servidor.Model.Venta;
import com.example.servidor.Services.ReporteService;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<List<ReporteFila>> productosMasVendidos(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.productosMasVendidos(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/productos-menor-existencia")
    public ResponseEntity<List<ReporteFila>> productosMenorExistencia() {

        return ResponseEntity.ok(
                reporteService.productosMenorExistencia()
        );
    }

    @GetMapping("/productos-mas-movimientos")
    public ResponseEntity<List<ReporteFila>> productosMasMovimientos(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.productosMasMovimientos(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/compras")
    public ResponseEntity<List<Compra>> compras(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.comprasPorRango(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/top-proveedores")
    public ResponseEntity<List<ReporteFila>> topProveedores(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.topProveedores(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/productos-mas-adquiridos")
    public ResponseEntity<List<ReporteFila>> productosMasAdquiridos(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.productosMasAdquiridos(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> ventas(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.ventasPorRango(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/top-clientes")
    public ResponseEntity<List<ReporteFila>> topClientes(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.topClientes(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/productos-mayores-ingresos")
    public ResponseEntity<List<ReporteFila>> productosMayoresIngresos(
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.productosMayoresIngresos(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/resumen-ventas")
    public ResponseEntity<List<ResumenPeriodo>> resumenVentas(
            @RequestParam(name = "agrupacion", defaultValue = "MES") String agrupacion,
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(
                reporteService.resumenVentas(
                        agrupacion,
                        fechaInicio,
                        fechaFin
                )
        );
    }
}