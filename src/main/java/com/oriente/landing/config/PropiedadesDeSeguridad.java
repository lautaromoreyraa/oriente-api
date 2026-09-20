package com.oriente.landing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuracion de seguridad, tipada.
 *
 * Los origenes de CORS son dos listas separadas y no una sola: la web publica y
 * el panel viven en dominios distintos, y la superficie de administracion no
 * tiene que aceptar pedidos desde el origen de la landing.
 */
@ConfigurationProperties(prefix = "oriente.seguridad")
public record PropiedadesDeSeguridad(
        Administrador administrador,
        Jwt jwt,
        Cors cors
) {

    public record Administrador(String usuario, String password) {
    }

    public record Jwt(String secret, long expiracionEnMilisegundos) {
    }

    public record Cors(List<String> origenesPublicos, List<String> origenesDeAdministracion) {
    }
}
