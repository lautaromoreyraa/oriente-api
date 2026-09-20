package com.oriente.landing.service.publico;

import com.oriente.landing.dto.publico.ContenidoDeLandingResponse;
import com.oriente.landing.dto.publico.ServicioPublicoResponse;

public interface ContenidoDeLandingService {

    /** Todo el contenido visible de la landing, en una sola lectura. */
    ContenidoDeLandingResponse obtenerContenido();

    ServicioPublicoResponse obtenerServicioPorSlug(String slug);
}
