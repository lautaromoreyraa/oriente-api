package com.oriente.landing.service.impl;

import com.oriente.landing.domain.entity.HeroSection;
import com.oriente.landing.dto.request.HeroSectionRequest;
import com.oriente.landing.dto.response.HeroSectionResponse;
import com.oriente.landing.exception.ResourceNotFoundException;
import com.oriente.landing.mapper.HeroSectionMapper;
import com.oriente.landing.repository.HeroSectionRepository;
import com.oriente.landing.service.HeroSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HeroSectionServiceImpl implements HeroSectionService {

    private final HeroSectionRepository repository;
    private final HeroSectionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<HeroSectionResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public HeroSectionResponse findById(Long id) {
        HeroSection entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HeroSection no encontrado con id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public HeroSectionResponse create(HeroSectionRequest request) {
        HeroSection entity = mapper.toEntity(request);
        HeroSection saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public HeroSectionResponse update(Long id, HeroSectionRequest request) {
        HeroSection entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HeroSection no encontrado con id: " + id));
        mapper.updateEntity(request, entity);
        HeroSection updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("HeroSection no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
