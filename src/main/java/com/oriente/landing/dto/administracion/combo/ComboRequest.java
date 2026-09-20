package com.oriente.landing.dto.administracion.combo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ComboRequest(
        @Size(max = 255) String slug,

        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 255) String titulo,

        @Size(max = 255) String bajada,
        String descripcion,
        @Size(max = 255) String etiqueta,
        Integer orden,
        Boolean activo,

        @Valid List<ItemDeComboRequest> items
) {
}
