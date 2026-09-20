package com.oriente.landing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Todo lo que no cayo en las cadenas anteriores se rechaza.
 *
 * Es el reemplazo del anyRequest().permitAll() que cerraba la configuracion
 * anterior: con esa regla, un controller nuevo montado fuera de /api/v1 quedaba
 * publico sin que nadie lo decidiera. Ahora el olvido falla cerrado.
 */
@Configuration
@Order(3)
public class SeguridadPorDefecto {

    @Bean
    public SecurityFilterChain cadenaPorDefecto(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(peticiones -> peticiones.anyRequest().denyAll());

        return http.build();
    }
}
