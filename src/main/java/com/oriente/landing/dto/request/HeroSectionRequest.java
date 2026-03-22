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
public class HeroSectionRequest {

    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    private String subtitle;

    private String ctaText;

    private String ctaUrl;

    private String backgroundImageUrl;

    private Boolean active = true;
}
