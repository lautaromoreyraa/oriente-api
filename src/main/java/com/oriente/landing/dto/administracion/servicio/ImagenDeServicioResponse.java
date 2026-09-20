package com.oriente.landing.dto.administracion.servicio;

public record ImagenDeServicioResponse(
        Long id,
        String imagenUrl,
        String imagenPublicId,
        String alt,
        String urlDePublicacion,
        Integer orden,
        Boolean activo
) {
}
