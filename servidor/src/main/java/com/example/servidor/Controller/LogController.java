package com.example.servidor.Controller;

import com.example.servidor.Model.LogSistema;
import com.example.servidor.Services.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<LogSistema>> listar(
            @RequestParam(name = "modulo", required = false) String modulo,
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {

        return ResponseEntity.ok(logService.listarLogs(modulo, fechaInicio, fechaFin));
    }
}
