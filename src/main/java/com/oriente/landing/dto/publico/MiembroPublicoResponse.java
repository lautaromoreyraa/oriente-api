package com.oriente.landing.dto.publico;

public record MiembroPublicoResponse(
        String nombre,
        String rol,
        String imagenUrl,
        String imagenAlt
) {
}
