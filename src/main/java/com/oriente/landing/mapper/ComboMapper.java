package com.oriente.landing.mapper;

import com.oriente.landing.domain.entity.Combo;
import com.oriente.landing.dto.request.ComboRequest;
import com.oriente.landing.dto.response.ComboResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = ComboItemMapper.class)
public interface ComboMapper {

    ComboResponse toResponse(Combo entity);

    Combo toEntity(ComboRequest request);

    void updateEntity(ComboRequest request, @MappingTarget Combo entity);

    List<ComboResponse> toResponseList(List<Combo> entities);
}
