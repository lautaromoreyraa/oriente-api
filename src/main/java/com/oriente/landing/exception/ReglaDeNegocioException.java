package com.oriente.landing.exception;

/**
 * La peticion es sintacticamente valida pero rompe una regla del dominio, como
 * un slug repetido. Traduce a 409, no a 400: el cliente no escribio mal, el
 * estado actual no lo permite.
 */
public class ReglaDeNegocioException extends RuntimeException {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
