package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.Hero;
import com.oriente.landing.dto.administracion.hero.HeroRequest;
import com.oriente.landing.dto.administracion.hero.HeroResponse;
import com.oriente.landing.dto.publico.HeroPublicoResponse;
import com.oriente.landing.mapper.HeroMapper;
import org.springframework.stereotype.Component;

@Component
public class HeroMapperImpl implements HeroMapper {

    @Override
    public void aplicar(HeroRequest request, Hero hero) {
        hero.setTitulo(request.titulo());
        hero.setSubtitulo(request.subtitulo());
        hero.setTextoDelCta(request.textoDelCta());
        hero.setUrlDelCta(request.urlDelCta());
        hero.setImagenDeFondoUrl(request.imagenDeFondoUrl());
        hero.setImagenDeFondoPublicId(request.imagenDeFondoPublicId());
        hero.setImagenDeFondoAlt(request.imagenDeFondoAlt());

        // activo es opcional en el request: si no viene, se respeta el valor guardado
        // en lugar de apagar la seccion por omision.
        if (request.activo() != null) {
            hero.setActivo(request.activo());
        }
    }

    @Override
    public HeroResponse aResponse(Hero hero) {
        return new HeroResponse(
                hero.getId(),
                hero.getTitulo(),
                hero.getSubtitulo(),
                hero.getTextoDelCta(),
                hero.getUrlDelCta(),
                hero.getImagenDeFondoUrl(),
                hero.getImagenDeFondoPublicId(),
                hero.getImagenDeFondoAlt(),
                hero.getActivo(),
                hero.getActualizadoEn()
        );
    }

    @Override
    public HeroPublicoResponse aPublico(Hero hero) {
        // El public_id de Cloudinary y las fechas no salen a la landing: son datos
        // de administracion.
        return new HeroPublicoResponse(
                hero.getTitulo(),
                hero.getSubtitulo(),
                hero.getTextoDelCta(),
                hero.getUrlDelCta(),
                hero.getImagenDeFondoUrl(),
                hero.getImagenDeFondoAlt()
        );
    }
}
