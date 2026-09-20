package com.oriente.landing.controller.publico;

import com.oriente.landing.dto.publico.ContenidoDeLandingResponse;
import com.oriente.landing.dto.publico.ServicioPublicoResponse;
import com.oriente.landing.service.publico.ContenidoDeLandingService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

/**
 * Lo que la web publica consume. Solo lectura.
 *
 * Las respuestas salen con Cache-Control para que Cloudflare las guarde en el
 * borde: la mayoria de las visitas no llegan a tocar esta aplicacion.
 * stale-while-revalidate hace que, si la API se cae, el borde siga sirviendo la
 * ultima version buena en lugar de mostrar un error.
 */
@RestController
@RequestMapping("/api/publico/v1")
public class ContenidoDeLandingController {

    private static final CacheControl CACHE_DE_CONTENIDO = CacheControl
            .maxAge(Duration.ofMinutes(5))
            .cachePublic()
            .staleWhileRevalidate(Duration.ofHours(24));

    private final ContenidoDeLandingService contenidoService;

    public ContenidoDeLandingController(ContenidoDeLandingService contenidoService) {
        this.contenidoService = contenidoService;
    }

    /** Todo el contenido de la landing en una sola llamada. */
    @GetMapping("/landing")
    public ResponseEntity<ContenidoDeLandingResponse> obtenerContenido() {
        return ResponseEntity.ok()
                .cacheControl(CACHE_DE_CONTENIDO)
                .body(contenidoService.obtenerContenido());
    }

    @GetMapping("/servicios/{slug}")
    public ResponseEntity<ServicioPublicoResponse> obtenerServicio(@PathVariable String slug) {
        return ResponseEntity.ok()
                .cacheControl(CACHE_DE_CONTENIDO)
                .body(contenidoService.obtenerServicioPorSlug(slug));
    }
}
