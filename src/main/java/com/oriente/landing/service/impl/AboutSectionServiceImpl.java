package com.oriente.landing.service.impl;

import com.oriente.landing.domain.entity.AboutSection;
import com.oriente.landing.dto.request.AboutSectionRequest;
import com.oriente.landing.dto.response.AboutSectionResponse;
import com.oriente.landing.exception.ResourceNotFoundException;
import com.oriente.landing.mapper.AboutSectionMapper;
import com.oriente.landing.repository.AboutSectionRepository;
import com.oriente.landing.service.AboutSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AboutSectionServiceImpl implements AboutSectionService {

    private final AboutSectionRepository repository;
    private final AboutSectionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<AboutSectionResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AboutSectionResponse findById(Long id) {
        AboutSection entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AboutSection no encontrado con id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public AboutSectionResponse create(AboutSectionRequest request) {
        AboutSection entity = mapper.toEntity(request);
        AboutSection saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public AboutSectionResponse update(Long id, AboutSectionRequest request) {
        AboutSection entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AboutSection no encontrado con id: " + id));
        mapper.updateEntity(request, entity);
        AboutSection updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("AboutSection no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
