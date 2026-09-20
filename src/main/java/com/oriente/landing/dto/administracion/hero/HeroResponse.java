package com.oriente.landing.dto.administracion.hero;

import java.time.LocalDateTime;

public record HeroResponse(
        Long id,
        String titulo,
        String subtitulo,
        String textoDelCta,
        String urlDelCta,
        String imagenDeFondoUrl,
        String imagenDeFondoPublicId,
        String imagenDeFondoAlt,
        Boolean activo,
        LocalDateTime actualizadoEn
) {
}
