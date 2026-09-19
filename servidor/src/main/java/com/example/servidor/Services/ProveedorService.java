package com.example.servidor.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.servidor.DB.ProveedorDB;
import com.example.servidor.Model.Proveedor;

@Service
public class ProveedorService {
     
    private final ProveedorDB proveedorDB;

    public ProveedorService(ProveedorDB proveedorDB){
        this.proveedorDB = proveedorDB;
    }

    public List<Proveedor> listarProveedores() {
        return proveedorDB.listarProveedores();
    }

    public List<Proveedor> listarProveedoresInactivos() {
        return proveedorDB.listarProveedoresInactivos();
    }

    public Proveedor buscarPorId(String idProveedor) {
        return proveedorDB.buscarPorId(idProveedor);
    }

    private String validarDatos(Proveedor proveedor) {
        if (proveedor.getNombreProveedor() == null || proveedor.getNombreProveedor().trim().isEmpty()) {
            return "El nombre del proveedor es obligatorio";
        }
        return null;
    }

    public String crearProveedor(Proveedor proveedor) {
        String error = validarDatos(proveedor);
        if (error != null) {
            return error;
        }

        String nuevoId = proveedorDB.generarSiguienteId();
        proveedor.setIdProveedor(nuevoId);

        boolean exito = proveedorDB.insertarProveedor(proveedor);
        if (!exito) {
            return "No se pudo guardar el proveedor";
        }
        return null;
    }

    public String actualizarProveedor(Proveedor proveedor) {
        if (proveedor.getIdProveedor() == null || proveedor.getIdProveedor().isEmpty()) {
            return "El id del proveedor es obligatorio";
        }

        Proveedor existente = proveedorDB.buscarPorId(proveedor.getIdProveedor());
        if (existente == null) {
            return "El proveedor no existe";
        }

        String error = validarDatos(proveedor);
        if (error != null) {
            return error;
        }

        boolean exito = proveedorDB.actualizarProveedor(proveedor);
        if (!exito) {
            return "No se pudo actualizar el proveedor";
        }
        return null;
    }

    public String desactivarProveedor(String idProveedor) {
        Proveedor existente = proveedorDB.buscarPorId(idProveedor);

        if (existente == null) {
            return "El proveedor no existe";
        }

        if (existente.getActivo() != null && !existente.getActivo()) {
            return "El proveedor ya se encuentra desactivado";
        }

        boolean exito = proveedorDB.desactivarProveedor(idProveedor);
        if (!exito) {
            return "No se pudo desactivar el proveedor";
        }
        return null;
    }
}
