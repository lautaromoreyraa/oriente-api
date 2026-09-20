package com.oriente.landing.dto.publico;

/**
 * Lo que la landing necesita para renderizar la fachada de una publicacion antes
 * de que el embed de Instagram cargue.
 */
public record PublicacionDeInstagramPublicaResponse(
        Long id,
        String url,
        String tipo,
        String titulo,
        String miniaturaUrl
) {
}
