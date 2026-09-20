package com.oriente.landing.dto.administracion.servicio;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * El servicio y su carrusel viajan juntos: el panel edita los dos en el mismo
 * formulario y guarda una sola vez.
 *
 * slug es opcional: si no viene, se deriva del titulo.
 */
public record ServicioRequest(
        @Size(max = 255) String slug,

        @NotNull(message = "La categoria es obligatoria")
        String categoria,

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 255) String titulo,

        String descripcion,
        String descripcionLarga,

        @Size(max = 500) String imagenUrl,
        @Size(max = 255) String imagenPublicId,
        @Size(max = 255) String imagenAlt,

        Integer orden,
        Boolean activo,

        @Valid List<ImagenDeServicioRequest> imagenes
) {
}
