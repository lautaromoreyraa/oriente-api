package com.oriente.landing.dto.publico;

import java.util.List;

public record ComboPublicoResponse(
        Long id,
        String slug,
        String titulo,
        String bajada,
        String descripcion,
        String etiqueta,
        Integer orden,
        List<String> incluye
) {
}
