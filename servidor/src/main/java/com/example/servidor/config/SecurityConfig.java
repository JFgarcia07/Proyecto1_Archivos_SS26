package com.example.servidor.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuracion central de seguridad del backend.
 *
 * Define que rutas son publicas (login) y cuales requieren autenticacion,
 * habilita CORS para que Angular pueda llamar a la API, expone el
 * codificador de contraseñas como Bean, y registra el JwtAuthFilter
 * para que cada peticion sea autenticada segun su token.
 *
 * @author jfgarcianata
 */
@Configuration
public class SecurityConfig {

    /**
     * Codificador de contraseñas como Bean unico, usado tanto para
     * cifrar en la carga inicial como para verificar en el login.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Permite que Angular (localhost:4200) llame al backend (localhost:8080)
     * sin ser bloqueado por la politica CORS del navegador.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(List.of("http://localhost:4200"));
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(List.of("*"));
        configuracion.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuracion);
        return source;
    }

    /**
     * Define las reglas de seguridad HTTP:
     * - /api/auth/** es publico (para poder hacer login sin token)
     * - cualquier otra ruta requiere autenticacion
     * - no se usan sesiones de servidor (stateless): la identidad
     *   viaja en el JWT en cada peticion
     * - el JwtAuthFilter se ejecuta antes del filtro estandar de
     *   Spring, para poder leer y validar el token de cada peticion
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}