package com.oriente.landing.dto.administracion.combo;

import java.time.LocalDateTime;
import java.util.List;

public record ComboResponse(
        Long id,
        String slug,
        String titulo,
        String bajada,
        String descripcion,
        String etiqueta,
        Integer orden,
        Boolean activo,
        List<ItemDeComboResponse> items,
        LocalDateTime actualizadoEn
) {
}
