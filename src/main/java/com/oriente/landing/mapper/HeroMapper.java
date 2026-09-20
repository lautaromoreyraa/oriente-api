package com.oriente.landing.mapper;

import com.oriente.landing.domain.Hero;
import com.oriente.landing.dto.administracion.hero.HeroRequest;
import com.oriente.landing.dto.administracion.hero.HeroResponse;
import com.oriente.landing.dto.publico.HeroPublicoResponse;

public interface HeroMapper {

    /** Copia los campos editables del request sobre la entidad. */
    void aplicar(HeroRequest request, Hero hero);

    HeroResponse aResponse(Hero hero);

    HeroPublicoResponse aPublico(Hero hero);
}
