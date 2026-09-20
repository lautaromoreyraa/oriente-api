package com.oriente.landing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Superficie publica: solo lectura, sin autenticacion.
 *
 * Cualquier metodo que no sea GET se rechaza aca mismo. Antes la regla era "los
 * GET son publicos y el resto pide token", lo que dejaba la puerta abierta a que
 * un POST publico apareciera por descuido en la misma ruta.
 */
@Configuration
@Order(1)
public class SeguridadDeLaApiPublica {

    private final PropiedadesDeSeguridad propiedades;

    public SeguridadDeLaApiPublica(PropiedadesDeSeguridad propiedades) {
        this.propiedades = propiedades;
    }

    @Bean
    public SecurityFilterChain cadenaPublica(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/publico/**")
                .cors(cors -> cors.configurationSource(corsPublico()))
                // Sin cookies ni sesion: no hay nada que un CSRF pueda aprovechar.
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(peticiones -> peticiones
                        .requestMatchers(HttpMethod.OPTIONS, "/api/publico/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/publico/**").permitAll()
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    private CorsConfigurationSource corsPublico() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(propiedades.cors().origenesPublicos());
        configuracion.setAllowedMethods(List.of("GET", "OPTIONS"));
        configuracion.setAllowedHeaders(List.of("Content-Type", "Accept"));
        configuracion.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/api/publico/**", configuracion);
        return fuente;
    }
}
