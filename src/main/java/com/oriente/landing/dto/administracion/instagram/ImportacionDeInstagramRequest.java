package com.oriente.landing.dto.administracion.instagram;

import com.oriente.landing.util.NormalizadorDeUrlDeInstagram;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

/** Alcanza con el link: el video, la portada y el tipo se traen de Instagram. */
public record ImportacionDeInstagramRequest(
        @NotNull(message = "Falta elegir el servicio")
        Long servicioId,

        @NotBlank(message = "La URL de la publicacion es obligatoria")
        @Size(max = 500)
        @Pattern(
                regexp = NormalizadorDeUrlDeInstagram.PATRON_DE_VALIDACION,
                message = NormalizadorDeUrlDeInstagram.MENSAJE_DE_VALIDACION
        )
        String url
) {
}
