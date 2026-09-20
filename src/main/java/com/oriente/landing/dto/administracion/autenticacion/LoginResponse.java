package com.oriente.landing.dto.administracion.autenticacion;

public record LoginResponse(
        String token,
        Long expiraEnMilisegundos,
        String usuario
) {
}
