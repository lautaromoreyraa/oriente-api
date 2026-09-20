package com.oriente.landing.dto.publico;

public record ContactoPublicoResponse(
        String whatsapp,
        String whatsappParaMostrar,
        String instagram,
        String direccion,
        String urlDeMaps,
        String embedDeMaps
) {
}
