package com.oriente.landing.service.administracion.combo.impl;

import com.oriente.landing.domain.Combo;
import com.oriente.landing.dto.administracion.combo.ComboRequest;
import com.oriente.landing.dto.administracion.combo.ComboResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.exception.ReglaDeNegocioException;
import com.oriente.landing.mapper.ComboMapper;
import com.oriente.landing.repository.ComboRepository;
import com.oriente.landing.service.administracion.combo.ComboService;
import com.oriente.landing.util.GeneradorDeSlug;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComboServiceImpl implements ComboService {

    private final ComboRepository comboRepository;
    private final ComboMapper comboMapper;

    public ComboServiceImpl(ComboRepository comboRepository, ComboMapper comboMapper) {
        this.comboRepository = comboRepository;
        this.comboMapper = comboMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComboResponse> listar() {
        return comboRepository.findAllByOrderByOrdenAsc().stream()
                .map(comboMapper::aResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ComboResponse obtenerPorId(Long id) {
        return comboMapper.aResponse(buscar(id));
    }

    @Override
    @Transactional
    public ComboResponse crear(ComboRequest request) {
        Combo combo = new Combo();
        combo.setSlug(resolverSlug(request, null));
        comboMapper.aplicar(request, combo);
        return comboMapper.aResponse(comboRepository.save(combo));
    }

    @Override
    @Transactional
    public ComboResponse actualizar(Long id, ComboRequest request) {
        Combo combo = buscar(id);
        combo.setSlug(resolverSlug(request, id));
        comboMapper.aplicar(request, combo);
        return comboMapper.aResponse(comboRepository.save(combo));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        comboRepository.delete(buscar(id));
    }

    private Combo buscar(Long id) {
        return comboRepository.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.porId("Combo", id));
    }

    private String resolverSlug(ComboRequest request, Long idActual) {
        String slug = (request.slug() == null || request.slug().isBlank())
                ? GeneradorDeSlug.desde(request.titulo())
                : GeneradorDeSlug.desde(request.slug());

        if (slug.isBlank()) {
            throw new ReglaDeNegocioException("No se pudo derivar un slug del titulo");
        }

        boolean repetido = (idActual == null)
                ? comboRepository.existsBySlug(slug)
                : comboRepository.existsBySlugAndIdNot(slug, idActual);

        if (repetido) {
            throw new ReglaDeNegocioException("Ya existe un combo con el slug '" + slug + "'");
        }

        return slug;
    }
}
