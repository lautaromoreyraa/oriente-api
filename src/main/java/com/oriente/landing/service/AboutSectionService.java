package com.oriente.landing.service;

import com.oriente.landing.dto.request.AboutSectionRequest;
import com.oriente.landing.dto.response.AboutSectionResponse;

import java.util.List;

public interface AboutSectionService {
    List<AboutSectionResponse> findAll();

    AboutSectionResponse findById(Long id);

    AboutSectionResponse create(AboutSectionRequest request);

    AboutSectionResponse update(Long id, AboutSectionRequest request);

    void delete(Long id);
}
