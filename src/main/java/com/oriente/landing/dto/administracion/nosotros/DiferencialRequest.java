package com.oriente.landing.dto.administracion.nosotros;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DiferencialRequest(
        @NotBlank(message = "El diferencial necesita un titulo")
        @Size(max = 120) String titulo,

        String descripcion,
        Integer orden,
        Boolean activo
) {
}
