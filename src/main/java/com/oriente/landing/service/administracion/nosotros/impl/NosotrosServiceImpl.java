package com.oriente.landing.service.administracion.nosotros.impl;

import com.oriente.landing.domain.Nosotros;
import com.oriente.landing.dto.administracion.nosotros.NosotrosRequest;
import com.oriente.landing.dto.administracion.nosotros.NosotrosResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.mapper.NosotrosMapper;
import com.oriente.landing.repository.NosotrosRepository;
import com.oriente.landing.service.administracion.nosotros.NosotrosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NosotrosServiceImpl implements NosotrosService {

    private final NosotrosRepository nosotrosRepository;
    private final NosotrosMapper nosotrosMapper;

    public NosotrosServiceImpl(NosotrosRepository nosotrosRepository, NosotrosMapper nosotrosMapper) {
        this.nosotrosRepository = nosotrosRepository;
        this.nosotrosMapper = nosotrosMapper;
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
        nosotrosMapper.aplicar(request, nosotros);
        return nosotrosMapper.aResponse(nosotrosRepository.save(nosotros));
    }
}
