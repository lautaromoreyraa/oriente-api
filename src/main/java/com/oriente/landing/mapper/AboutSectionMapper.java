package com.oriente.landing.mapper;

import com.oriente.landing.domain.entity.AboutSection;
import com.oriente.landing.dto.request.AboutSectionRequest;
import com.oriente.landing.dto.response.AboutSectionResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AboutSectionMapper {

    AboutSectionResponse toResponse(AboutSection entity);

    AboutSection toEntity(AboutSectionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(AboutSectionRequest request, @MappingTarget AboutSection entity);

    List<AboutSectionResponse> toResponseList(List<AboutSection> entities);

}
