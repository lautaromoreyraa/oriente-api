package com.oriente.landing.dto.administracion.nosotros;

public record DiferencialResponse(
        Long id,
        String titulo,
        String descripcion,
        Integer orden,
        Boolean activo
) {
}
