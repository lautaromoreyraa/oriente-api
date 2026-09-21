package com.oriente.landing.exception;

/**
 * Un servicio del que depende la operacion (Instagram, Cloudinary) respondio con
 * un error o no respondio. Traduce a 502: el pedido estaba bien y la
 * configuracion tambien, el problema esta del otro lado.
 */
public class ServicioExternoException extends RuntimeException {

    public ServicioExternoException(String mensaje) {
        super(mensaje);
    }

    public ServicioExternoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
