package com.oriente.landing.service.administracion.hero;

import com.oriente.landing.dto.administracion.hero.HeroRequest;
import com.oriente.landing.dto.administracion.hero.HeroResponse;

public interface HeroService {

    HeroResponse obtener();

    /** El hero es unico: si todavia no existe, la primera edicion lo crea. */
    HeroResponse guardar(HeroRequest request);
}
