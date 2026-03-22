package com.oriente.landing.mapper;

import com.oriente.landing.domain.entity.HeroSection;
import com.oriente.landing.dto.request.HeroSectionRequest;
import com.oriente.landing.dto.response.HeroSectionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeroSectionMapper {

    HeroSectionResponse toResponse(HeroSection entity);

    HeroSection toEntity(HeroSectionRequest request);

    void updateEntity(HeroSectionRequest request, @MappingTarget HeroSection entity);

    List<HeroSectionResponse> toResponseList(List<HeroSection> entities);
}
