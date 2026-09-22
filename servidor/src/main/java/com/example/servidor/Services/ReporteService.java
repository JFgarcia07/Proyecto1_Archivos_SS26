package com.example.servidor.Services;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.servidor.DB.ReporteDB;
import com.example.servidor.Model.Compra;
import com.example.servidor.Model.ReporteFila;
import com.example.servidor.Model.ResumenPeriodo;
import com.example.servidor.Model.Venta;

/**
 * Logica del modulo de reportes.
 *
 * Convierte las fechas recibidas (texto yyyy-MM-dd) a fechas SQL.
 * Si el usuario no indica una fecha, se usa un limite muy amplio,
 * de modo que el reporte incluya todos los registros.
 *
 * @author jfgarcianata
 */
@Service
public class ReporteService {

    private static final String FECHA_MINIMA = "1900-01-01";
    private static final String FECHA_MAXIMA = "2999-12-31";

    private final ReporteDB reporteDB;

    public ReporteService(ReporteDB reporteDB) {
        this.reporteDB = reporteDB;
    }

    public List<ReporteFila> productosMasVendidos(String fechaInicio, String fechaFin) {
        return reporteDB.productosMasVendidos(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<ReporteFila> productosMenorExistencia() {
        return reporteDB.productosMenorExistencia();
    }

    public List<ReporteFila> productosMasMovimientos(String fechaInicio, String fechaFin) {
        return reporteDB.productosMasMovimientos(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<Compra> comprasPorRango(String fechaInicio, String fechaFin) {
        return reporteDB.comprasPorRango(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<ReporteFila> topProveedores(String fechaInicio, String fechaFin) {
        return reporteDB.topProveedores(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<ReporteFila> productosMasAdquiridos(String fechaInicio, String fechaFin) {
        return reporteDB.productosMasAdquiridos(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<Venta> ventasPorRango(String fechaInicio, String fechaFin) {
        return reporteDB.ventasPorRango(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<ReporteFila> topClientes(String fechaInicio, String fechaFin) {
        return reporteDB.topClientes(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<ReporteFila> productosMayoresIngresos(String fechaInicio, String fechaFin) {
        return reporteDB.productosMayoresIngresos(fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    public List<ResumenPeriodo> resumenVentas(String agrupacion, String fechaInicio, String fechaFin) {
        return reporteDB.resumenVentas(agrupacion, fechaDesde(fechaInicio), fechaHasta(fechaFin));
    }

    /**
     * Fecha inicial del filtro; si no viene, se usa la fecha minima.
     */
    private Date fechaDesde(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return Date.valueOf(FECHA_MINIMA);
        }
        return Date.valueOf(fecha);
    }

    /**
     * Fecha final del filtro; si no viene, se usa la fecha maxima.
     */
    private Date fechaHasta(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return Date.valueOf(FECHA_MAXIMA);
        }
        return Date.valueOf(fecha);
    }
}