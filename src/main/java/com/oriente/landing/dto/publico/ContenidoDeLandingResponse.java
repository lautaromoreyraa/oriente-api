package com.oriente.landing.dto.publico;

import java.util.List;

/**
 * Todo el contenido de la landing en una sola respuesta.
 *
 * El frontend renderiza la pagina en el servidor: con un endpoint por seccion
 * tendria que esperar seis viajes antes de poder devolver el HTML. Aca hace uno.
 */
public record ContenidoDeLandingResponse(
        HeroPublicoResponse hero,
        List<ServicioPublicoResponse> servicios,
        List<ComboPublicoResponse> combos,
        NosotrosPublicoResponse nosotros,
        List<PublicacionDeInstagramPublicaResponse> instagram,
        ContactoPublicoResponse contacto
) {
}
