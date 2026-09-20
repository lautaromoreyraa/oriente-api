package com.oriente.landing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Credenciales de Cloudinary para firmar uploads.
 *
 * Pueden venir vacias: la aplicacion arranca igual y solo falla el endpoint de
 * firma, con un mensaje que dice que falta configurar. Un valor de ejemplo por
 * defecto seria peor: firmaria mal y el error aparecería recien al subir.
 */
@ConfigurationProperties(prefix = "oriente.cloudinary")
public record PropiedadesDeCloudinary(
        String nombreDeLaNube,
        String apiKey,
        String apiSecret,
        String carpeta
) {

    public boolean estaConfigurado() {
        return tieneValor(nombreDeLaNube) && tieneValor(apiKey) && tieneValor(apiSecret);
    }

    private boolean tieneValor(String valor) {
        return valor != null && !valor.isBlank();
    }
}
