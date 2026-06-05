package com.oriente.landing.service.impl;

import com.oriente.landing.domain.entity.Combo;
import com.oriente.landing.domain.entity.ComboItem;
import com.oriente.landing.dto.request.ComboRequest;
import com.oriente.landing.dto.response.ComboResponse;
import com.oriente.landing.exception.ResourceNotFoundException;
import com.oriente.landing.mapper.ComboMapper;
import com.oriente.landing.repository.ComboRepository;
import com.oriente.landing.service.ComboService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ComboServiceImpl implements ComboService {

    private final ComboRepository repository;
    private final ComboMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ComboResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ComboResponse findById(Long id) {
        Combo entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Combo no encontrado con id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ComboResponse findBySlug(String slug) {
        Combo entity = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Combo no encontrado con slug: " + slug));
        return mapper.toResponse(entity);
    }

    @Override
    public ComboResponse create(ComboRequest request) {
        Combo entity = mapper.toEntity(request);
        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setCombo(entity));
        }
        Combo saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public ComboResponse update(Long id, ComboRequest request) {
        Combo entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Combo no encontrado con id: " + id));

        // Actualizar campos básicos
        mapper.updateEntity(request, entity);

        // Actualizar items del combo
        entity.getItems().clear();
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            List<ComboItem> newItems = request.getItems().stream()
                    .map(itemRequest -> {
                        ComboItem item = new ComboItem();
                        item.setDescription(itemRequest.getDescription());
                        item.setDisplayOrder(itemRequest.getDisplayOrder());
                        item.setCombo(entity);
                        return item;
                    })
                    .collect(Collectors.toList());
            List<ComboItem> existingItems = entity.getItems();
            existingItems.clear();
            existingItems.addAll(newItems);
        }

        Combo updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Combo no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
