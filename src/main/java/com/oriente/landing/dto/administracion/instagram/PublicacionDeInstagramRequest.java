package com.oriente.landing.dto.administracion.instagram;

import com.oriente.landing.util.NormalizadorDeUrlDeInstagram;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PublicacionDeInstagramRequest(
        // Instagram entrega el mismo contenido bajo varias formas segun de donde se
        // copie el link: /p/CODIGO desde la web, /usuario/p/CODIGO desde un perfil
        // en la app, y /share/ALGO desde el boton Compartir. Se aceptan todas -antes
        // solo entraba la primera, y las otras dos son las que da el telefono- y el
        // servicio las lleva a una forma unica antes de guardarlas.
        @NotBlank(message = "La URL de la publicacion es obligatoria")
        @Size(max = 500)
        @Pattern(
                regexp = NormalizadorDeUrlDeInstagram.PATRON_DE_VALIDACION,
                message = NormalizadorDeUrlDeInstagram.MENSAJE_DE_VALIDACION
        )
        String url,

        String tipo,
        @Size(max = 255) String titulo,
        @Size(max = 500) String miniaturaUrl,
        @Size(max = 255) String miniaturaPublicId,
        @Size(max = 500) String videoUrl,
        @Size(max = 255) String videoPublicId,
        Integer orden,
        Boolean activo
) {
}
