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

import com.example.servidor.Model.Cliente;
import com.example.servidor.Services.ClienteService;
import com.example.servidor.Services.LogService;


@RestController 
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final LogService logService;

    public ClienteController(ClienteService clienteService, LogService logService) {
        this.clienteService = clienteService;
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<Cliente>> listarInactivos() {
        return ResponseEntity.ok(clienteService.listarClientesInactivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscarPorId(id);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody Cliente cliente) {
        String error = clienteService.crearCliente(cliente);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        logService.registrar(LogService.ACCION_CREAR, LogService.MODULO_CLIENTES,
            "Creo el cliente " + cliente.getNombre() + " (NIT " + cliente.getNit() + ")");
        return ResponseEntity.ok("Cliente creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id, @RequestBody Cliente cliente) {
        cliente.setIdCliente(id);
        String error = clienteService.actualizarCliente(cliente);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        logService.registrar(LogService.ACCION_ACTUALIZAR, LogService.MODULO_CLIENTES,
            "Actualizo el cliente " + id + " - " + cliente.getNombre());
        return ResponseEntity.ok("Cliente actualizado correctamente");
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivar(@PathVariable Integer id) {
        String error = clienteService.desactivarCliente(id);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }
        logService.registrar(LogService.ACCION_DESACTIVAR, LogService.MODULO_CLIENTES,
            "Desactivo el cliente " + id);
        return ResponseEntity.ok("Cliente desactivado correctamente");
    }
}
