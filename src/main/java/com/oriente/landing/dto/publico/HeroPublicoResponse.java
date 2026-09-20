package com.oriente.landing.dto.publico;

public record HeroPublicoResponse(
        String titulo,
        String subtitulo,
        String textoDelCta,
        String urlDelCta,
        String imagenDeFondoUrl,
        String imagenDeFondoAlt
) {
}
