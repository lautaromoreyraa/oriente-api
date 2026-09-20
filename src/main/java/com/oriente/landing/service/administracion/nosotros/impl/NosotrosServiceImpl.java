package com.oriente.landing.service.administracion.nosotros.impl;

import com.oriente.landing.domain.Nosotros;
import com.oriente.landing.dto.administracion.nosotros.NosotrosRequest;
import com.oriente.landing.dto.administracion.nosotros.NosotrosResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.mapper.NosotrosMapper;
import com.oriente.landing.repository.NosotrosRepository;
import com.oriente.landing.service.administracion.imagen.BorradorDeImagenes;
import com.oriente.landing.service.administracion.imagen.ImagenesQuedaronHuerfanas;
import com.oriente.landing.service.administracion.nosotros.NosotrosService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class NosotrosServiceImpl implements NosotrosService {

    private final NosotrosRepository nosotrosRepository;
    private final NosotrosMapper nosotrosMapper;
    private final ApplicationEventPublisher eventos;

    public NosotrosServiceImpl(NosotrosRepository nosotrosRepository, NosotrosMapper nosotrosMapper,
                               ApplicationEventPublisher eventos) {
        this.nosotrosRepository = nosotrosRepository;
        this.nosotrosMapper = nosotrosMapper;
        this.eventos = eventos;
    }

    @Override
    @Transactional(readOnly = true)
    public NosotrosResponse obtener() {
        Nosotros nosotros = nosotrosRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RecursoNoEncontradoException("Todavia no hay contenido de nosotros cargado"));
        return nosotrosMapper.aResponse(nosotros);
    }

    @Override
    @Transactional
    public NosotrosResponse guardar(NosotrosRequest request) {
        Nosotros nosotros = nosotrosRepository.findFirstByOrderByIdAsc().orElseGet(Nosotros::new);

        // Quitar a alguien del equipo o cambiar su foto deja archivos sin referencia.
        Set<String> imagenesPrevias = imagenesDe(nosotros);

        nosotrosMapper.aplicar(request, nosotros);
        Nosotros guardado = nosotrosRepository.save(nosotros);

        eventos.publishEvent(ImagenesQuedaronHuerfanas.de(
                BorradorDeImagenes.loQueSobra(imagenesPrevias, imagenesDe(guardado))));

        return nosotrosMapper.aResponse(guardado);
    }

    /** La foto de la seccion mas las del equipo. */
    private Set<String> imagenesDe(Nosotros nosotros) {
        Set<String> publicIds = new HashSet<>();
        if (nosotros.getImagenPublicId() != null) {
            publicIds.add(nosotros.getImagenPublicId());
        }
        nosotros.getEquipo().stream()
                .map(miembro -> miembro.getImagenPublicId())
                .filter(publicId -> publicId != null)
                .forEach(publicIds::add);
        return publicIds;
    }
}
