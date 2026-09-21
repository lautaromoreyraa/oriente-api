package com.oriente.landing.service.administracion.instagram.impl;

import com.oriente.landing.service.administracion.instagram.ResolvedorDeEnlaces;
import com.oriente.landing.util.NormalizadorDeUrlDeInstagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

/**
 * Resuelve los links del boton Compartir de Instagram.
 *
 * Esos links no dicen a que publicacion apuntan: son un redirector. Se los sigue
 * una sola vez, al guardar, y se persiste el destino. Hacerlo en cada visita
 * ataria la landing a que Instagram responda.
 *
 * Si no se puede resolver no se rompe nada: el llamador guarda el link tal como
 * vino. La fachada con la miniatura y el enlace sigue funcionando; lo unico que se
 * pierde es el embed.
 */
@Service
public class ResolvedorDeEnlacesDeInstagram implements ResolvedorDeEnlaces {

    private static final Logger log = LoggerFactory.getLogger(ResolvedorDeEnlacesDeInstagram.class);

    private static final Duration ESPERA_MAXIMA = Duration.ofSeconds(5);

    private final HttpClient clienteHttp;

    public ResolvedorDeEnlacesDeInstagram() {
        this.clienteHttp = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(ESPERA_MAXIMA)
                .build();
    }

    @Override
    public Optional<String> resolver(String urlParaCompartir) {
        if (!NormalizadorDeUrlDeInstagram.esEnlaceParaCompartir(urlParaCompartir)) {
            return Optional.empty();
        }

        try {
            HttpRequest pedido = HttpRequest.newBuilder(URI.create(urlParaCompartir.trim()))
                    // HEAD alcanza: sólo interesa a dónde termina el redirect, no el
                    // cuerpo de la página.
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(ESPERA_MAXIMA)
                    // Sin un user agent de navegador, Instagram suele devolver una
                    // página de login en lugar de seguir al contenido.
                    .header("User-Agent", "Mozilla/5.0 (compatible; OrienteBot/1.0)")
                    .build();

            HttpResponse<Void> respuesta = clienteHttp.send(pedido, HttpResponse.BodyHandlers.discarding());
            String destino = respuesta.uri().toString();

            Optional<String> publicacion = NormalizadorDeUrlDeInstagram.normalizar(destino);
            if (publicacion.isEmpty()) {
                log.info("El enlace para compartir no llevo a una publicacion: {}", destino);
            }
            return publicacion;

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (Exception ex) {
            log.info("No se pudo seguir el enlace para compartir {}: {}", urlParaCompartir, ex.getMessage());
            return Optional.empty();
        }
    }
}
