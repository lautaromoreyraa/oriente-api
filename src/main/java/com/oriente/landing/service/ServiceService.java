package com.oriente.landing.service;

import com.oriente.landing.dto.request.ServiceRequest;
import com.oriente.landing.dto.response.ServiceResponse;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> findAll();

    ServiceResponse findById(Long id);

    ServiceResponse findBySlug(String slug);

    ServiceResponse create(ServiceRequest request);

    ServiceResponse update(Long id, ServiceRequest request);

    void delete(Long id);
}
