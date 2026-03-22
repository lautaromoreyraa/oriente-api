package com.oriente.landing.mapper;

import com.oriente.landing.domain.entity.ComboItem;
import com.oriente.landing.dto.request.ComboItemRequest;
import com.oriente.landing.dto.response.ComboItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComboItemMapper {

    ComboItemResponse toResponse(ComboItem entity);

    ComboItem toEntity(ComboItemRequest request);

    void updateEntity(ComboItemRequest request, @MappingTarget ComboItem entity);

    List<ComboItemResponse> toResponseList(List<ComboItem> entities);
}
