package com.example.servidor.Services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.servidor.Model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Genera y valida los tokens JWT. Sus datos viajan
 * dentro del token en cada peticion.
 *
 * @author jfgarcianata
 */
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secreto;

    @Value("${jwt.expiration-ms}")
    private long tiempoExpiracionMs;

    public String generarToken(Usuario usuario) {
        SecretKey clave = Keys.hmacShaKeyFor(secreto.getBytes());
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + tiempoExpiracionMs);

        String token = Jwts.builder()
                .subject(usuario.getEmail())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("idRol", usuario.getIdRol())
                .issuedAt(fechaActual)
                .expiration(fechaExpiracion)
                .signWith(clave)
                .compact();

        return token;
    }

    public Claims validarToken(String token) {
        SecretKey clave = Keys.hmacShaKeyFor(secreto.getBytes());

        Claims claims = Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims;
    }
}
