package com.oriente.landing.dto.publico;

import java.util.List;

public record NosotrosPublicoResponse(
        String titulo,
        String cuerpo,
        String imagenUrl,
        String imagenAlt,
        List<EstadisticaPublicaResponse> estadisticas,
        List<DiferencialPublicoResponse> diferenciales,
        List<MiembroPublicoResponse> equipo
) {
}
