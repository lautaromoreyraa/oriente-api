package com.oriente.landing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Superficie de administracion: todo pide token, incluidas las lecturas.
 *
 * El panel muestra contenido desactivado y datos que la landing no expone, asi
 * que un GET de aca no es publico como el de la otra cadena.
 */
@Configuration
@EnableWebSecurity
@Order(2)
public class SeguridadDeLaApiDeAdministracion {

    private final PropiedadesDeSeguridad propiedades;
    private final FiltroDeAutenticacionJwt filtroJwt;
    private final PuntoDeEntradaNoAutenticado puntoDeEntrada;

    public SeguridadDeLaApiDeAdministracion(PropiedadesDeSeguridad propiedades,
                                           FiltroDeAutenticacionJwt filtroJwt,
                                           PuntoDeEntradaNoAutenticado puntoDeEntrada) {
        this.propiedades = propiedades;
        this.filtroJwt = filtroJwt;
        this.puntoDeEntrada = puntoDeEntrada;
    }

    @Bean
    public PasswordEncoder codificadorDePassword() {
        return new BCryptPasswordEncoder();
    }

    /**
     * El AuthenticationManager se arma a mano en lugar de tomarlo de
     * AuthenticationConfiguration: con dos cadenas de seguridad, dejar que Spring
     * lo deduzca vuelve ambiguo cual proveedor termina usando cada una.
     */
    @Bean
    public AuthenticationManager gestorDeAutenticacion(DetallesDelAdministrador detalles,
                                                       PasswordEncoder codificador) {
        DaoAuthenticationProvider proveedor = new DaoAuthenticationProvider(detalles);
        proveedor.setPasswordEncoder(codificador);
        return new ProviderManager(proveedor);
    }

    @Bean
    public SecurityFilterChain cadenaDeAdministracion(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/admin/**")
                .cors(cors -> cors.configurationSource(corsDeAdministracion()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(peticiones -> peticiones
                        .requestMatchers(HttpMethod.OPTIONS, "/api/admin/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admin/v1/auth/login").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .exceptionHandling(manejo -> manejo.authenticationEntryPoint(puntoDeEntrada))
                .addFilterBefore(filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsDeAdministracion() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(propiedades.cors().origenesDeAdministracion());
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Lista explicita en lugar de "*": el panel solo necesita estos dos headers.
        configuracion.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuracion.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/api/admin/**", configuracion);
        return fuente;
    }
}
