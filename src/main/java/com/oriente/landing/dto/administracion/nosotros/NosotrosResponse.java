package com.oriente.landing.dto.administracion.nosotros;

import java.time.LocalDateTime;
import java.util.List;

public record NosotrosResponse(
        Long id,
        String titulo,
        String cuerpo,
        String imagenUrl,
        String imagenPublicId,
        String imagenAlt,
        Boolean activo,
        List<EstadisticaResponse> estadisticas,
        List<DiferencialResponse> diferenciales,
        List<MiembroDelEquipoResponse> equipo,
        LocalDateTime actualizadoEn
) {
}
