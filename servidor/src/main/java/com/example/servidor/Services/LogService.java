package com.example.servidor.Services;

import com.example.servidor.DB.LogDB;
import com.example.servidor.Model.LogSistema;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class LogService {
    
    public static final String MODULO_AUTENTICACION = "AUTENTICACION";
    public static final String MODULO_PRODUCTOS = "PRODUCTOS";
    public static final String MODULO_PROVEEDORES = "PROVEEDORES";
    public static final String MODULO_CLIENTES = "CLIENTES";
    public static final String MODULO_COMPRAS = "COMPRAS";
    public static final String MODULO_VENTAS = "VENTAS";

    public static final String ACCION_INICIO_SESION = "INICIO_SESION";
    public static final String ACCION_CREAR = "CREAR";
    public static final String ACCION_ACTUALIZAR = "ACTUALIZAR";
    public static final String ACCION_DESACTIVAR = "DESACTIVAR";
    public static final String ACCION_REGISTRAR = "REGISTRAR";
    public static final String ACCION_GENERAR_FACTURA = "GENERAR_FACTURA";

    private static final int LONGITUD_MAXIMA_DETALLE = 500;

    private final LogDB logDB;

    public LogService(LogDB logDB) {
        this.logDB = logDB;
    }

    /**
     * Registra una accion del usuario autenticado en la peticion actual.
     */
    public void registrar(String accion, String modulo, String detalle) {
        String idUsuario = obtenerUsuarioActual();

        if (idUsuario == null) {
            System.err.println("No se registro el log: no hay usuario autenticado");
            return;
        }

        registrarConUsuario(idUsuario, accion, modulo, detalle);
    }

    /**
     * Registra una accion indicando el usuario explicitamente.
     * Se usa en el login, donde todavia no existe un token.
     */
    public void registrarConUsuario(String idUsuario, String accion, String modulo, String detalle) {
        try {
            logDB.insertarLog(idUsuario, accion, modulo, recortar(detalle));
        } catch (DataAccessException e) {
            System.err.println("Error al registrar el log: " + e.getMessage());
        }
    }

    /**
     * Obtiene el id del usuario autenticado a partir del contexto
     * de seguridad de la peticion actual.
     *
     * @return el idUsuario, o null si no hay usuario autenticado
     */
    public String obtenerUsuarioActual() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();

        if (autenticacion == null) {
            return null;
        }

        String nombre = autenticacion.getName();

        if (nombre == null || nombre.equals("anonymousUser")) {
            return null;
        }
        return nombre;
    }

    public List<LogSistema> listarLogs(String modulo, String fechaInicio, String fechaFin) {
        return logDB.listarLogs(modulo, fechaInicio, fechaFin);
    }

    /**
     * Da formato a un monto para incluirlo en el detalle de un log.
     */
    public String formatearMonto(Double monto) {
        if (monto == null) {
            return "Q 0.00";
        }
        return "Q " + String.format(Locale.US, "%,.2f", monto);
    }

    private String recortar(String texto) {
        if (texto == null) {
            return null;
        }
        if (texto.length() > LONGITUD_MAXIMA_DETALLE) {
            return texto.substring(0, LONGITUD_MAXIMA_DETALLE);
        }
        return texto;
    }
}
