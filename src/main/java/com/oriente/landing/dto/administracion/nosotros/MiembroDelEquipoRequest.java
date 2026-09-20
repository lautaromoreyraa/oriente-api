package com.oriente.landing.dto.administracion.nosotros;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MiembroDelEquipoRequest(
        @NotBlank(message = "El miembro del equipo necesita un nombre")
        @Size(max = 120) String nombre,

        @Size(max = 120) String rol,
        @Size(max = 500) String imagenUrl,
        @Size(max = 255) String imagenPublicId,
        @Size(max = 255) String imagenAlt,
        Integer orden,
        Boolean activo
) {
}
