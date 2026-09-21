package com.oriente.landing.service.administracion.instagram.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oriente.landing.config.PropiedadesDeInstagram;
import com.oriente.landing.exception.ConfiguracionIncompletaException;
import com.oriente.landing.exception.ServicioExternoException;
import com.oriente.landing.service.administracion.instagram.FuenteDeInstagram;
import com.oriente.landing.util.NormalizadorDeUrlDeInstagram;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Lee las publicaciones de la cuenta con la API oficial de Instagram.
 *
 * La API no permite pedir una publicacion por su codigo: se recorre la lista de
 * la cuenta, de la mas nueva a la mas vieja, hasta encontrarla. Lo que se carga
 * en la landing suele ser reciente, asi que casi siempre aparece en la primera
 * pagina.
 */
@Service
public class FuenteDeInstagramGraph implements FuenteDeInstagram {

    private static final String CAMPOS = "id,media_type,media_url,thumbnail_url,permalink";
    private static final int POR_PAGINA = 50;

    /** Mil publicaciones: mas atras que eso no se va a buscar nada para la portada. */
    private static final int PAGINAS_MAXIMAS = 20;

    private final PropiedadesDeInstagram propiedades;
    private final RestClient clienteHttp;

    public FuenteDeInstagramGraph(PropiedadesDeInstagram propiedades) {
        this.propiedades = propiedades;
        this.clienteHttp = RestClient.create();
    }

    @Override
    public Optional<ContenidoDeInstagram> buscar(String codigo) {
        if (!propiedades.estaConfigurado()) {
            throw new ConfiguracionIncompletaException(
                    "La cuenta de Instagram no esta conectada: falta INSTAGRAM_ACCESS_TOKEN en el servidor");
        }

        URI siguiente = URI.create(propiedades.urlDeLaApi() + "/me/media"
                + "?fields=" + CAMPOS
                + "&limit=" + POR_PAGINA
                + "&access_token=" + propiedades.tokenDeAcceso());

        for (int pagina = 0; pagina < PAGINAS_MAXIMAS && siguiente != null; pagina++) {
            Pagina respuesta = pedir(siguiente);

            Optional<ContenidoDeInstagram> encontrada = respuesta.medios().stream()
                    .filter(medio -> NormalizadorDeUrlDeInstagram.codigo(medio.permalink())
                            .filter(codigo::equals)
                            .isPresent())
                    .findFirst()
                    .map(FuenteDeInstagramGraph::aContenido);
            if (encontrada.isPresent()) {
                return encontrada;
            }

            siguiente = respuesta.siguiente().map(URI::create).orElse(null);
        }
        return Optional.empty();
    }

    private Pagina pedir(URI uri) {
        try {
            Pagina pagina = clienteHttp.get().uri(uri).retrieve().body(Pagina.class);
            if (pagina == null) {
                throw new ServicioExternoException("Instagram respondio vacio");
            }
            return pagina;
        } catch (ServicioExternoException ex) {
            throw ex;
        } catch (Exception ex) {
            // El mensaje de la excepcion no se propaga: puede traer la URL, y la URL
            // lleva el token.
            throw new ServicioExternoException(
                    "Instagram no respondio. Si sigue pasando, puede que el token haya vencido");
        }
    }

    /**
     * En un video, media_url es el archivo y thumbnail_url la portada. En una foto
     * o un carrusel, media_url es la imagen (la primera, en el carrusel).
     */
    static ContenidoDeInstagram aContenido(Medio medio) {
        String enlace = NormalizadorDeUrlDeInstagram.normalizar(medio.permalink()).orElse(medio.permalink());
        if ("VIDEO".equals(medio.tipo())) {
            return new ContenidoDeInstagram(enlace, medio.mediaUrl(), medio.miniaturaUrl());
        }
        return new ContenidoDeInstagram(enlace, null, medio.mediaUrl());
    }

    record Pagina(
            @JsonProperty("data") List<Medio> medios,
            @JsonProperty("paging") Paginado paginado
    ) {
        Pagina {
            medios = medios == null ? List.of() : medios;
        }

        Optional<String> siguiente() {
            return Optional.ofNullable(paginado).map(Paginado::next).filter(next -> !next.isBlank());
        }
    }

    record Paginado(String next) {
    }

    record Medio(
            String id,
            @JsonProperty("media_type") String tipo,
            @JsonProperty("media_url") String mediaUrl,
            @JsonProperty("thumbnail_url") String miniaturaUrl,
            String permalink
    ) {
    }
}
