package com.oriente.landing.service.impl;

import com.oriente.landing.domain.entity.Service;
import com.oriente.landing.dto.request.ServiceRequest;
import com.oriente.landing.dto.response.ServiceResponse;
import com.oriente.landing.exception.ResourceNotFoundException;
import com.oriente.landing.mapper.ServiceMapper;
import com.oriente.landing.repository.ServiceRepository;
import com.oriente.landing.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository repository;
    private final ServiceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse findById(Long id) {
        Service entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service no encontrado con id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse findBySlug(String slug) {
        Service entity = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Service no encontrado con slug: " + slug));
        return mapper.toResponse(entity);
    }

    @Override
    public ServiceResponse create(ServiceRequest request) {
        Service entity = mapper.toEntity(request);
        Service saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public ServiceResponse update(Long id, ServiceRequest request) {
        Service entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service no encontrado con id: " + id));
        mapper.updateEntity(request, entity);
        Service updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Service no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
