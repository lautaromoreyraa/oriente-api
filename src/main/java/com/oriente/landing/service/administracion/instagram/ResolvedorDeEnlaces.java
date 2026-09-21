package com.oriente.landing.service.administracion.instagram;

import java.util.Optional;

public interface ResolvedorDeEnlaces {

    /**
     * Sigue un link del boton Compartir hasta la publicacion a la que apunta.
     *
     * Devuelve vacio si no se puede resolver: Instagram fuera de alcance, el link
     * vencido, o una respuesta que no lleva a ninguna publicacion.
     */
    Optional<String> resolver(String urlParaCompartir);
}
