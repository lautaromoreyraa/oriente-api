package com.oriente.landing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Size(max = 20, message = "El valor de la estadística no puede superar 20 caracteres")
    private String stat1Value;

    @Size(max = 60, message = "La etiqueta no puede superar 60 caracteres")
    private String stat1Label;

    @Size(max = 20)
    private String stat2Value;

    @Size(max = 60)
    private String stat2Label;

    @Size(max = 20)
    private String stat3Value;

    @Size(max = 60)
    private String stat3Label;

    // ── Diferenciales editables ───────────────────────────
    @Size(max = 120)
    private String diff1Title;

    private String diff1Desc;

    @Size(max = 120)
    private String diff2Title;

    private String diff2Desc;

    @Size(max = 120)
    private String diff3Title;

    private String diff3Desc;

    @Size(max = 120)
    private String diff4Title;

    private String diff4Desc;
}