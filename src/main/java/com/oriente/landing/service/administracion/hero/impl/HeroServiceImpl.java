package com.oriente.landing.service.administracion.hero.impl;

import com.oriente.landing.domain.Hero;
import com.oriente.landing.dto.administracion.hero.HeroRequest;
import com.oriente.landing.dto.administracion.hero.HeroResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.mapper.HeroMapper;
import com.oriente.landing.repository.HeroRepository;
import com.oriente.landing.service.administracion.hero.HeroService;
import com.oriente.landing.service.administracion.imagen.BorradorDeImagenes;
import com.oriente.landing.service.administracion.imagen.ImagenesQuedaronHuerfanas;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final HeroMapper heroMapper;
    private final ApplicationEventPublisher eventos;

    public HeroServiceImpl(HeroRepository heroRepository, HeroMapper heroMapper,
                           ApplicationEventPublisher eventos) {
        this.heroRepository = heroRepository;
        this.heroMapper = heroMapper;
        this.eventos = eventos;
    }

    @Override
    @Transactional(readOnly = true)
    public HeroResponse obtener() {
        Hero hero = heroRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RecursoNoEncontradoException("Todavia no hay un hero cargado"));
        return heroMapper.aResponse(hero);
    }

    @Override
    @Transactional
    public HeroResponse guardar(HeroRequest request) {
        Hero hero = heroRepository.findFirstByOrderByIdAsc().orElseGet(Hero::new);
        String imagenPrevia = hero.getImagenDeFondoPublicId();

        heroMapper.aplicar(request, hero);
        Hero guardado = heroRepository.save(hero);

        // Reemplazar la imagen de portada deja la anterior sin quien la nombre.
        eventos.publishEvent(ImagenesQuedaronHuerfanas.de(BorradorDeImagenes.loQueSobra(
                imagenPrevia == null ? Set.of() : Set.of(imagenPrevia),
                guardado.getImagenDeFondoPublicId() == null ? Set.of() : Set.of(guardado.getImagenDeFondoPublicId()))));

        return heroMapper.aResponse(guardado);
    }
}
