package com.oriente.landing.dto.administracion.hero;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HeroRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 255, message = "El titulo no puede pasar de 255 caracteres")
        String titulo,

        @Size(max = 255) String subtitulo,
        @Size(max = 255) String textoDelCta,
        @Size(max = 255) String urlDelCta,
        @Size(max = 500) String imagenDeFondoUrl,
        @Size(max = 255) String imagenDeFondoPublicId,
        @Size(max = 255) String imagenDeFondoAlt,
        Boolean activo
) {
}
