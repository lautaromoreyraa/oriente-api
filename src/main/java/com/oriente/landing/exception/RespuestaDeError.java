package com.oriente.landing.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Forma unica de los errores de la API.
 *
 * errores solo viaja cuando hay validaciones por campo; el resto del tiempo se
 * omite (la configuracion de Jackson descarta los nulos).
 */
public record RespuestaDeError(
        String mensaje,
        Integer estado,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime momento,
        Map<String, String> errores
) {

    public static RespuestaDeError de(String mensaje, int estado) {
        return new RespuestaDeError(mensaje, estado, LocalDateTime.now(), null);
    }

    public static RespuestaDeError deValidacion(String mensaje, int estado, Map<String, String> errores) {
        return new RespuestaDeError(mensaje, estado, LocalDateTime.now(), errores);
    }
}
