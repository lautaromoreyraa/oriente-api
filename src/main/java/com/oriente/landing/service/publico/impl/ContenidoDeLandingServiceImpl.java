package com.oriente.landing.service.publico.impl;

import com.oriente.landing.domain.Servicio;
import com.oriente.landing.dto.publico.ComboPublicoResponse;
import com.oriente.landing.dto.publico.ContactoPublicoResponse;
import com.oriente.landing.dto.publico.ContenidoDeLandingResponse;
import com.oriente.landing.dto.publico.HeroPublicoResponse;
import com.oriente.landing.dto.publico.NosotrosPublicoResponse;
import com.oriente.landing.dto.publico.PublicacionDeInstagramPublicaResponse;
import com.oriente.landing.dto.publico.ServicioPublicoResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.mapper.ComboMapper;
import com.oriente.landing.mapper.HeroMapper;
import com.oriente.landing.mapper.InformacionDeContactoMapper;
import com.oriente.landing.mapper.NosotrosMapper;
import com.oriente.landing.mapper.PublicacionDeInstagramMapper;
import com.oriente.landing.mapper.ServicioMapper;
import com.oriente.landing.repository.ComboRepository;
import com.oriente.landing.repository.HeroRepository;
import com.oriente.landing.repository.InformacionDeContactoRepository;
import com.oriente.landing.repository.NosotrosRepository;
import com.oriente.landing.repository.PublicacionDeInstagramRepository;
import com.oriente.landing.repository.ServicioRepository;
import com.oriente.landing.service.publico.ContenidoDeLandingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContenidoDeLandingServiceImpl implements ContenidoDeLandingService {

    private final HeroRepository heroRepository;
    private final ServicioRepository servicioRepository;
    private final ComboRepository comboRepository;
    private final NosotrosRepository nosotrosRepository;
    private final PublicacionDeInstagramRepository publicacionRepository;
    private final InformacionDeContactoRepository contactoRepository;

    private final HeroMapper heroMapper;
    private final ServicioMapper servicioMapper;
    private final ComboMapper comboMapper;
    private final NosotrosMapper nosotrosMapper;
    private final PublicacionDeInstagramMapper publicacionMapper;
    private final InformacionDeContactoMapper contactoMapper;

    public ContenidoDeLandingServiceImpl(
            HeroRepository heroRepository,
            ServicioRepository servicioRepository,
            ComboRepository comboRepository,
            NosotrosRepository nosotrosRepository,
            PublicacionDeInstagramRepository publicacionRepository,
            InformacionDeContactoRepository contactoRepository,
            HeroMapper heroMapper,
            ServicioMapper servicioMapper,
            ComboMapper comboMapper,
            NosotrosMapper nosotrosMapper,
            PublicacionDeInstagramMapper publicacionMapper,
            InformacionDeContactoMapper contactoMapper) {
        this.heroRepository = heroRepository;
        this.servicioRepository = servicioRepository;
        this.comboRepository = comboRepository;
        this.nosotrosRepository = nosotrosRepository;
        this.publicacionRepository = publicacionRepository;
        this.contactoRepository = contactoRepository;
        this.heroMapper = heroMapper;
        this.servicioMapper = servicioMapper;
        this.comboMapper = comboMapper;
        this.nosotrosMapper = nosotrosMapper;
        this.publicacionMapper = publicacionMapper;
        this.contactoMapper = contactoMapper;
    }

    /**
     * La transaccion envuelve todo el armado porque open-in-view esta apagado: las
     * listas perezosas (imagenes, items, equipo) se resuelven aca y no mas tarde en
     * el controller, donde la sesion ya estaria cerrada.
     *
     * Cada seccion puede faltar sin que la respuesta falle: una landing recien
     * instalada todavia no tiene contenido cargado, y devolver null en una seccion
     * es mejor que un 500.
     */
    @Override
    @Transactional(readOnly = true)
    public ContenidoDeLandingResponse obtenerContenido() {
        HeroPublicoResponse hero = heroRepository.findFirstByActivoTrueOrderByIdAsc()
                .map(heroMapper::aPublico)
                .orElse(null);

        List<ServicioPublicoResponse> servicios = servicioRepository.findAllByActivoTrueOrderByOrdenAsc()
                .stream()
                .map(servicioMapper::aPublico)
                .toList();

        List<ComboPublicoResponse> combos = comboRepository.findAllByActivoTrueOrderByOrdenAsc()
                .stream()
                .map(comboMapper::aPublico)
                .toList();

        NosotrosPublicoResponse nosotros = nosotrosRepository.findFirstByActivoTrueOrderByIdAsc()
                .map(nosotrosMapper::aPublico)
                .orElse(null);

        List<PublicacionDeInstagramPublicaResponse> instagram =
                publicacionRepository.findAllByActivoTrueOrderByOrdenAsc()
                        .stream()
                        .map(publicacionMapper::aPublico)
                        .toList();

        ContactoPublicoResponse contacto = contactoRepository.findFirstByOrderByIdAsc()
                .map(contactoMapper::aPublico)
                .orElse(null);

        return new ContenidoDeLandingResponse(hero, servicios, combos, nosotros, instagram, contacto);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioPublicoResponse obtenerServicioPorSlug(String slug) {
        Servicio servicio = servicioRepository.findBySlug(slug)
                .filter(encontrado -> Boolean.TRUE.equals(encontrado.getActivo()))
                .orElseThrow(() -> RecursoNoEncontradoException.porSlug("Servicio", slug));

        return servicioMapper.aPublico(servicio);
    }
}
