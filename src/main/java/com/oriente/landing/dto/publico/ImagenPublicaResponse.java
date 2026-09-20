package com.oriente.landing.dto.publico;

/** Una imagen del carrusel, con el link al post de Instagram si lo tiene. */
public record ImagenPublicaResponse(
        String imagenUrl,
        String alt,
        String urlDePublicacion
) {
}
