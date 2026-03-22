package com.oriente.landing.dto.request;

import com.oriente.landing.domain.enums.ServiceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {

    @NotBlank(message = "El slug no puede estar vacío")
    private String slug;

    @NotNull(message = "La categoría no puede ser nula")
    private ServiceCategory category;

    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    private String description;

    private String iconPath;

    private Integer displayOrder = 0;

    private Boolean active = true;
}
