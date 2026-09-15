package com.example.servidor.Controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.servidor.Model.Usuario;
import com.example.servidor.Services.JwtService;
import com.example.servidor.Services.ValidarLogIn;

@RestController
@RequestMapping("/api/auth")
public class LogInController {
    private final ValidarLogIn validarLogIn;
    private final JwtService jwtService;

    public LogInController(ValidarLogIn validarLogIn, JwtService jwtService) {
        this.validarLogIn = validarLogIn;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String body){
        JSONObject json = new JSONObject(body);
        String email = json.getString("email");
        String password = json.getString("password");
        
        Usuario usuario = validarLogIn.validarCredenciales(email, password);
    
        JSONObject respuesta = new JSONObject();
        if(usuario != null){
            String token = jwtService.generarToken(usuario);
            respuesta.put("token", token);
            respuesta.put("succes",true);
            respuesta.put("idUsuario", usuario.getIdUsuario());
            respuesta.put("idRol", usuario.getIdRol());
        } else {
            respuesta.put("succes",false);
            respuesta.put("message", "Credenciales inválidas");
        }
        return ResponseEntity.ok(respuesta.toString());
    }
}
