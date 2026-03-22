package com.oriente.landing.service;

import com.oriente.landing.dto.request.HeroSectionRequest;
import com.oriente.landing.dto.response.HeroSectionResponse;

import java.util.List;

public interface HeroSectionService {
    List<HeroSectionResponse> findAll();

    HeroSectionResponse findById(Long id);

    HeroSectionResponse create(HeroSectionRequest request);

    HeroSectionResponse update(Long id, HeroSectionRequest request);

    void delete(Long id);
}
