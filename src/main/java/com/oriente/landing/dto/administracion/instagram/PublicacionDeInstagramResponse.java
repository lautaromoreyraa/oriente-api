package com.oriente.landing.dto.administracion.instagram;

import java.time.LocalDateTime;

public record PublicacionDeInstagramResponse(
        Long id,
        String url,
        String tipo,
        String titulo,
        String miniaturaUrl,
        String miniaturaPublicId,
        Integer orden,
        Boolean activo,
        LocalDateTime actualizadoEn
) {
}
