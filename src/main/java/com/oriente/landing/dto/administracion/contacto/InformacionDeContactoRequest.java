package com.oriente.landing.dto.administracion.contacto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InformacionDeContactoRequest(
        @NotBlank(message = "El whatsapp es obligatorio")
        @Size(max = 30)
        @Pattern(regexp = "^\\+?[0-9]{8,20}$", message = "Solo digitos, opcionalmente con + adelante")
        String whatsapp,

        @Size(max = 40) String whatsappParaMostrar,
        @Size(max = 60) String instagram,
        @Size(max = 255) String direccion,
        @Size(max = 500) String urlDeMaps,
        String embedDeMaps
) {
}
