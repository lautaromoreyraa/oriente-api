package com.oriente.landing.service.administracion.contacto.impl;

import com.oriente.landing.domain.InformacionDeContacto;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoRequest;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.mapper.InformacionDeContactoMapper;
import com.oriente.landing.repository.InformacionDeContactoRepository;
import com.oriente.landing.service.administracion.contacto.InformacionDeContactoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformacionDeContactoServiceImpl implements InformacionDeContactoService {

    private final InformacionDeContactoRepository contactoRepository;
    private final InformacionDeContactoMapper contactoMapper;

    public InformacionDeContactoServiceImpl(
            InformacionDeContactoRepository contactoRepository,
            InformacionDeContactoMapper contactoMapper) {
        this.contactoRepository = contactoRepository;
        this.contactoMapper = contactoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public InformacionDeContactoResponse obtener() {
        InformacionDeContacto contacto = contactoRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RecursoNoEncontradoException("Todavia no hay datos de contacto cargados"));
        return contactoMapper.aResponse(contacto);
    }

    @Override
    @Transactional
    public InformacionDeContactoResponse guardar(InformacionDeContactoRequest request) {
        InformacionDeContacto contacto = contactoRepository.findFirstByOrderByIdAsc()
                .orElseGet(InformacionDeContacto::new);
        contactoMapper.aplicar(request, contacto);
        return contactoMapper.aResponse(contactoRepository.save(contacto));
    }
}
