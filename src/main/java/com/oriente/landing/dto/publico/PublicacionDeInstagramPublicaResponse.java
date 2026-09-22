package com.oriente.landing.dto.publico;

/** Un reel o un posteo, como lo muestra la tarjeta de su servicio. */
public record PublicacionDeInstagramPublicaResponse(
        Long id,
        String url,
        String tipo,
        String titulo,
        /** El cuadro que se ve mientras el video carga, o la foto si no hay video. */
        String miniaturaUrl,
        String videoUrl
) {
}
