package com.oriente.landing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Acceso a la API de Instagram de la cuenta del consultorio.
 *
 * El token puede venir vacio: la aplicacion arranca igual y solo falla la
 * importacion de publicaciones, con un mensaje que dice que falta conectar la
 * cuenta.
 */
@ConfigurationProperties(prefix = "oriente.instagram")
public record PropiedadesDeInstagram(
        String urlDeLaApi,
        String tokenDeAcceso
) {

    public boolean estaConfigurado() {
        return tokenDeAcceso != null && !tokenDeAcceso.isBlank();
    }
}
