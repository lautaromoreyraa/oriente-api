package com.oriente.landing.dto.publico;

import java.util.List;

/**
 * Un servicio como lo ve la landing.
 *
 * No se exponen los public_id de Cloudinary ni las fechas: son datos de
 * administracion que el visitante no necesita.
 */
public record ServicioPublicoResponse(
        Long id,
        String slug,
        String categoria,
        String titulo,
        String descripcion,
        String descripcionLarga,
        String imagenUrl,
        String imagenAlt,
        Integer orden,
        List<ImagenPublicaResponse> imagenes
) {
}
