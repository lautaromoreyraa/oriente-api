package com.oriente.landing.exception;

/** Se pidio algo por id o slug y no existe. Traduce a 404. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public static RecursoNoEncontradoException porId(String recurso, Long id) {
        return new RecursoNoEncontradoException(recurso + " con id " + id + " no existe");
    }

    public static RecursoNoEncontradoException porSlug(String recurso, String slug) {
        return new RecursoNoEncontradoException(recurso + " con slug '" + slug + "' no existe");
    }
}
