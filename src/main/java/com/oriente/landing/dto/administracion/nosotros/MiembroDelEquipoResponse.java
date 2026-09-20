package com.oriente.landing.dto.administracion.nosotros;

public record MiembroDelEquipoResponse(
        Long id,
        String nombre,
        String rol,
        String imagenUrl,
        String imagenPublicId,
        String imagenAlt,
        Integer orden,
        Boolean activo
) {
}
