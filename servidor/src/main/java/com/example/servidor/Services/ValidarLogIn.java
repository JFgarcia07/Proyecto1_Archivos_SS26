package com.example.servidor.Services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.servidor.DB.LogInDB;
import com.example.servidor.Model.Usuario;

/**
 * Valida las credenciales de login: busca el usuario por email
 * y compara el password ingresado contra el hash guardado.
 *
 * @author jfgarcianata
 */
@Service
public class ValidarLogIn {
    private final LogInDB logInDB;
    private final BCryptPasswordEncoder passwordEncoder;

    public ValidarLogIn(LogInDB logInDB, BCryptPasswordEncoder passwordEncoder) {
        this.logInDB = logInDB;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario validarCredenciales(String email, String password) {
        Usuario usuario = logInDB.buscarUsuarioPorEmail(email);
        if(usuario == null) {
            return null; 
        }
        if(!usuario.isActivo()) {
            return null; 
        }
        if(!passwordEncoder.matches(password, usuario.getPassword())) {
            return null;
        }
        return usuario;
    }
}
