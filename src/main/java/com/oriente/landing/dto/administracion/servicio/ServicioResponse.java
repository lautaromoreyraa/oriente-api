package com.oriente.landing.dto.administracion.servicio;

import java.time.LocalDateTime;
import java.util.List;

public record ServicioResponse(
        Long id,
        String slug,
        String categoria,
        String titulo,
        String descripcion,
        String descripcionLarga,
        String imagenUrl,
        String imagenPublicId,
        String imagenAlt,
        Integer orden,
        Boolean activo,
        List<ImagenDeServicioResponse> imagenes,
        LocalDateTime actualizadoEn
) {
}
