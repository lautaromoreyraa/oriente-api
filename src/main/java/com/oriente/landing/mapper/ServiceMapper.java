package com.oriente.landing.mapper;

import com.oriente.landing.domain.entity.Service;
import com.oriente.landing.dto.request.ServiceRequest;
import com.oriente.landing.dto.response.ServiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceResponse toResponse(Service entity);

    Service toEntity(ServiceRequest request);

    void updateEntity(ServiceRequest request, @MappingTarget Service entity);

    List<ServiceResponse> toResponseList(List<Service> entities);
}
