package com.oriente.landing.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Responde 401 cuando el pedido llega sin token o con un token que no sirve.
 *
 * Sin esto Spring Security contesta 403 a todo, y la diferencia importa del lado
 * del cliente: el panel trata el 401 como "la sesion se termino" y vuelve al
 * login, mientras que un 403 lo deja con un token muerto en la mano y sin saber
 * que hacer. 403 queda para el caso distinto: autenticado, pero sin permiso.
 */
@Component
public class PuntoDeEntradaNoAutenticado implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException excepcion) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // El JSON se arma a mano porque este punto corre en la cadena de filtros,
        // antes de que exista un controller: no hay conversores de mensajes todavia.
        String momento = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        response.getWriter().write(
                "{\"mensaje\":\"No autenticado\",\"estado\":401,\"momento\":\"" + momento + "\"}");
    }
}
