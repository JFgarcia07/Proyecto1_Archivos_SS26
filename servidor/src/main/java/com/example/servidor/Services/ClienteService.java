package com.example.servidor.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.servidor.DB.ClienteDB;
import com.example.servidor.Model.Cliente;

@Service 
public class ClienteService {
    
    private final ClienteDB clienteDB;

    public ClienteService(ClienteDB clienteDB) {
        this.clienteDB = clienteDB;
    }

    public List<Cliente> listarClientes() {
        return clienteDB.listarClientes();
    }

    public List<Cliente> listarClientesInactivos() {
        return clienteDB.listarClientesInactivos();
    }

    public Cliente buscarPorId(Integer idCliente) {
        return clienteDB.buscarPorId(idCliente);
    }

    public String crearCliente(Cliente cliente) {
        String error = validarDatos(cliente);
        if (error != null) {
            return error;
        }

        if (clienteDB.existeNit(cliente.getNit(), null)) {
            return "Ya existe un cliente registrado con ese NIT";
        }

        boolean exito = clienteDB.insertarCliente(cliente);
        if (!exito) {
            return "No se pudo guardar el cliente";
        }
        return null;
    }

    public String actualizarCliente(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            return "El id del cliente es obligatorio";
        }

        Cliente existente = clienteDB.buscarPorId(cliente.getIdCliente());
        if (existente == null) {
            return "El cliente no existe";
        }

        String error = validarDatos(cliente);
        if (error != null) {
            return error;
        }

        if (clienteDB.existeNit(cliente.getNit(), cliente.getIdCliente())) {
            return "Ya existe otro cliente registrado con ese NIT";
        }

        boolean exito = clienteDB.actualizarCliente(cliente);
        if (!exito) {
            return "No se pudo actualizar el cliente";
        }
        return null;
    }

    public String desactivarCliente(Integer idCliente) {
        Cliente existente = clienteDB.buscarPorId(idCliente);

        if (existente == null) {
            return "El cliente no existe";
        }

        if (existente.getActivo() != null && !existente.getActivo()) {
            return "El cliente ya se encuentra desactivado";
        }

        boolean exito = clienteDB.desactivarCliente(idCliente);
        if (!exito) {
            return "No se pudo desactivar el cliente";
        }
        return null;
    }

    private String validarDatos(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            return "El nombre del cliente es obligatorio";
        }
        if (cliente.getNit() == null || cliente.getNit().trim().isEmpty()) {
            return "El NIT es obligatorio";
        }
        return null;
    }
}
