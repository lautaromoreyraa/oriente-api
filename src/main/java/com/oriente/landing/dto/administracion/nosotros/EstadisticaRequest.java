package com.oriente.landing.dto.administracion.nosotros;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstadisticaRequest(
        @NotBlank(message = "La estadistica necesita un valor")
        @Size(max = 20) String valor,

        @NotBlank(message = "La estadistica necesita una etiqueta")
        @Size(max = 60) String etiqueta,

        Integer orden,
        Boolean activo
) {
}
