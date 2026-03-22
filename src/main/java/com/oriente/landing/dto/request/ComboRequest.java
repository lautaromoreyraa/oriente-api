package com.oriente.landing.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboRequest {

    //ComboRequest
    @NotBlank(message = "El slug no puede estar vacío")
    private String slug;

    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    private String tagline;

    private String description;

    private String badge;

    private Integer displayOrder = 0;

    private Boolean active = true;

    @Valid
    private List<ComboItemRequest> items;
}
