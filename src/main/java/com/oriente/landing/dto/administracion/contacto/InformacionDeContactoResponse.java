package com.oriente.landing.dto.administracion.contacto;

import java.time.LocalDateTime;

public record InformacionDeContactoResponse(
        Long id,
        String whatsapp,
        String whatsappParaMostrar,
        String instagram,
        String direccion,
        String urlDeMaps,
        String embedDeMaps,
        LocalDateTime actualizadoEn
) {
}
