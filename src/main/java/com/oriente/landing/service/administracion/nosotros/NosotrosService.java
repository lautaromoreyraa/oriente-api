package com.oriente.landing.service.administracion.nosotros;

import com.oriente.landing.dto.administracion.nosotros.NosotrosRequest;
import com.oriente.landing.dto.administracion.nosotros.NosotrosResponse;

public interface NosotrosService {

    NosotrosResponse obtener();

    /** Guarda la seccion completa: texto, estadisticas, diferenciales y equipo. */
    NosotrosResponse guardar(NosotrosRequest request);
}
