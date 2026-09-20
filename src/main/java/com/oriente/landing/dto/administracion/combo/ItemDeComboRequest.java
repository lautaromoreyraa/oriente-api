package com.oriente.landing.dto.administracion.combo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemDeComboRequest(
        @NotBlank(message = "El item necesita una descripcion")
        @Size(max = 255) String descripcion,
        Integer orden
) {
}
