package com.oriente.landing.exception;

/**
 * Una funcion opcional no tiene su configuracion cargada (por ejemplo, subir
 * imagenes sin las credenciales de Cloudinary). Traduce a 503: no es culpa de
 * quien llama y se arregla configurando el entorno.
 */
public class ConfiguracionIncompletaException extends RuntimeException {

    public ConfiguracionIncompletaException(String mensaje) {
        super(mensaje);
    }
}
