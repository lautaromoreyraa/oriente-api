package com.oriente.landing.dto.administracion.servicio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ImagenDeServicioRequest(
        @NotBlank(message = "La imagen necesita una URL")
        @Size(max = 500) String imagenUrl,

        @Size(max = 255) String imagenPublicId,
        @Size(max = 255) String alt,

        // El link al post de Instagram del que salio la foto. La imagen servida es
        // siempre la de Cloudinary: las del CDN de Instagram expiran.
        @Size(max = 500) String urlDePublicacion,

        Integer orden,
        Boolean activo
) {
}
