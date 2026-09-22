package com.oriente.landing.dto.administracion.instagram;

import java.time.LocalDateTime;

public record PublicacionDeInstagramResponse(
        Long id,
        Long servicioId,
        String url,
        String tipo,
        String titulo,
        String miniaturaUrl,
        String miniaturaPublicId,
        String videoUrl,
        String videoPublicId,
        Integer orden,
        Boolean activo,
        LocalDateTime actualizadoEn
) {
}
