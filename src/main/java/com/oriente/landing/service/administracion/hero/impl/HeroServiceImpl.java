package com.oriente.landing.service.administracion.hero.impl;

import com.oriente.landing.domain.Hero;
import com.oriente.landing.dto.administracion.hero.HeroRequest;
import com.oriente.landing.dto.administracion.hero.HeroResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.mapper.HeroMapper;
import com.oriente.landing.repository.HeroRepository;
import com.oriente.landing.service.administracion.hero.HeroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;
    private final HeroMapper heroMapper;

    public HeroServiceImpl(HeroRepository heroRepository, HeroMapper heroMapper) {
        this.heroRepository = heroRepository;
        this.heroMapper = heroMapper;
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
        heroMapper.aplicar(request, hero);
        return heroMapper.aResponse(heroRepository.save(hero));
    }
}
