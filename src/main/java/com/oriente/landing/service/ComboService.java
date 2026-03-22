package com.oriente.landing.service;

import com.oriente.landing.dto.request.ComboRequest;
import com.oriente.landing.dto.response.ComboResponse;

import java.util.List;

public interface ComboService {
    List<ComboResponse> findAll();

    ComboResponse findById(Long id);

    ComboResponse findBySlug(String slug);

    ComboResponse create(ComboRequest request);

    ComboResponse update(Long id, ComboRequest request);

    void delete(Long id);
}
