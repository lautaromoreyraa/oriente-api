package com.oriente.landing.dto.administracion.instagram;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PublicacionDeInstagramRequest(
        // Se valida la forma de la URL porque el embed oficial solo funciona con
        // links de post, reel o tv: cualquier otra cosa muestra un hueco vacio en
        // la landing y el error aparece recien en produccion.
        @NotBlank(message = "La URL de la publicacion es obligatoria")
        @Size(max = 500)
        @Pattern(
                regexp = "^https://(www\\.)?instagram\\.com/(p|reel|reels|tv)/[A-Za-z0-9_-]+/?.*$",
                message = "Tiene que ser un link de instagram.com a un post, reel o tv"
        )
        String url,

        String tipo,
        @Size(max = 255) String titulo,
        @Size(max = 500) String miniaturaUrl,
        @Size(max = 255) String miniaturaPublicId,
        Integer orden,
        Boolean activo
) {
}
