package com.oriente.landing.dto.administracion.nosotros;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * La seccion completa en un solo guardado: texto, estadisticas, diferenciales y
 * equipo. Es como el panel ya la edita, en un formulario unico.
 */
public record NosotrosRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 255) String titulo,

        String cuerpo,

        @Size(max = 500) String imagenUrl,
        @Size(max = 255) String imagenPublicId,
        @Size(max = 255) String imagenAlt,
        Boolean activo,

        @Valid List<EstadisticaRequest> estadisticas,
        @Valid List<DiferencialRequest> diferenciales,
        @Valid List<MiembroDelEquipoRequest> equipo
) {
}
