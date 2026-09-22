package com.example.servidor.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.servidor.DB.InventarioDB;
import com.example.servidor.Model.MovimientoInventario;
import com.example.servidor.Model.Producto;

@Service
public class InventarioService {
    @Value("${inventario.stock-minimo:17}")
    private double stockMinimo;

    private final InventarioDB inventarioDB;

    public InventarioService(InventarioDB inventarioDB) {
        this.inventarioDB = inventarioDB;
    }

    public double getStockMinimo() {
        return stockMinimo;
    }

    public List<Producto> listarExistencias() {
        return inventarioDB.listarExistencias();
    }

    /**
     * Productos activos cuya existencia es igual o menor al minimo.
     */
    public List<Producto> listarAlertas() {
        return inventarioDB.listarStockBajo(stockMinimo);
    }

    public List<MovimientoInventario> listarMovimientos(String idProducto, String tipoMovimiento,
                                                       String fechaInicio, String fechaFin) {
        return inventarioDB.listarMovimientos(idProducto, tipoMovimiento, fechaInicio, fechaFin);
    }
}
