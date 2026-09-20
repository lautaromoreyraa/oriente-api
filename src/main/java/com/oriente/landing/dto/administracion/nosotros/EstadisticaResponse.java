package com.oriente.landing.dto.administracion.nosotros;

public record EstadisticaResponse(
        Long id,
        String valor,
        String etiqueta,
        Integer orden,
        Boolean activo
) {
}
