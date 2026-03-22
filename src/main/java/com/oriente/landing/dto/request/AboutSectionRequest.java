package com.oriente.landing.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutSectionRequest {

    @NotBlank(message = "El encabezado no puede estar vacío")
    private String heading;

    private String body;

    private String imageUrl;

    private Boolean active = true;
}
