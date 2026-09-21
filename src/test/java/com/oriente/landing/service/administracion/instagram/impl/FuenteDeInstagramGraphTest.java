package com.oriente.landing.service.administracion.instagram.impl;

import com.oriente.landing.config.PropiedadesDeInstagram;
import com.oriente.landing.exception.ConfiguracionIncompletaException;
import com.oriente.landing.exception.ServicioExternoException;
import com.oriente.landing.service.administracion.instagram.FuenteDeInstagram.ContenidoDeInstagram;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FuenteDeInstagramGraphTest {

    private HttpServer servidor;
    private String base;
    private final AtomicInteger pedidos = new AtomicInteger();

    @BeforeEach
    void levantarInstagramFalso() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        base = "http://localhost:" + servidor.getAddress().getPort();

        servidor.createContext("/me/media", intercambio -> {
            pedidos.incrementAndGet();
            boolean segundaPagina = intercambio.getRequestURI().getQuery().contains("after=pagina2");
            String cuerpo = segundaPagina
                    ? """
                      {"data":[{"id":"3","media_type":"IMAGE",
                        "media_url":"https://cdn.test/foto.jpg",
                        "permalink":"https://www.instagram.com/p/FOTO3/"}]}
                      """
                    : """
                      {"data":[{"id":"1","media_type":"VIDEO",
                        "media_url":"https://cdn.test/reel.mp4",
                        "thumbnail_url":"https://cdn.test/portada.jpg",
                        "permalink":"https://www.instagram.com/reel/REEL1/"}],
                       "paging":{"next":"%s/me/media?after=pagina2"}}
                      """.formatted(base);
            byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
            intercambio.getResponseHeaders().add("Content-Type", "application/json");
            intercambio.sendResponseHeaders(200, bytes.length);
            try (OutputStream salida = intercambio.getResponseBody()) {
                salida.write(bytes);
            }
        });
        servidor.createContext("/caida", intercambio -> {
            intercambio.sendResponseHeaders(500, -1);
            intercambio.close();
        });
        servidor.start();
    }

    @AfterEach
    void apagar() {
        servidor.stop(0);
    }

    private FuenteDeInstagramGraph fuente(String url, String token) {
        return new FuenteDeInstagramGraph(new PropiedadesDeInstagram(url, token));
    }

    @Test
    @DisplayName("de un video trae el archivo y la portada, y el link canonico")
    void traeUnVideo() {
        ContenidoDeInstagram contenido = fuente(base, "token").buscar("REEL1").orElseThrow();

        assertTrue(contenido.esVideo());
        assertEquals("https://cdn.test/reel.mp4", contenido.videoUrl());
        assertEquals("https://cdn.test/portada.jpg", contenido.imagenUrl());
        assertEquals("https://www.instagram.com/reel/REEL1/", contenido.enlace());
    }

    @Test
    @DisplayName("sigue a la pagina siguiente si no esta en la primera")
    void recorreLasPaginas() {
        ContenidoDeInstagram contenido = fuente(base, "token").buscar("FOTO3").orElseThrow();

        assertFalse(contenido.esVideo());
        assertNull(contenido.videoUrl());
        assertEquals("https://cdn.test/foto.jpg", contenido.imagenUrl());
        assertEquals(2, pedidos.get());
    }

    @Test
    @DisplayName("una publicacion que no es de la cuenta da vacio")
    void noLaEncuentra() {
        Optional<ContenidoDeInstagram> contenido = fuente(base, "token").buscar("DEOTRACUENTA");

        assertTrue(contenido.isEmpty());
    }

    @Test
    @DisplayName("sin token no sale ningun pedido: falta configurar")
    void sinToken() {
        assertThrows(ConfiguracionIncompletaException.class, () -> fuente(base, "").buscar("REEL1"));
        assertEquals(0, pedidos.get());
    }

    @Test
    @DisplayName("si Instagram falla, el error no muestra la URL, que lleva el token")
    void noFiltraElToken() {
        ServicioExternoException error = assertThrows(ServicioExternoException.class,
                () -> fuente(base + "/caida", "secreto").buscar("REEL1"));

        assertFalse(error.getMessage().contains("secreto"));
    }
}
