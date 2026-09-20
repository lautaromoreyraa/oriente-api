package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.PublicacionDeInstagram;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;
import com.oriente.landing.dto.publico.PublicacionDeInstagramPublicaResponse;
import com.oriente.landing.enumeration.TipoDePublicacion;
import com.oriente.landing.mapper.PublicacionDeInstagramMapper;
import org.springframework.stereotype.Component;

@Component
public class PublicacionDeInstagramMapperImpl implements PublicacionDeInstagramMapper {

    @Override
    public void aplicar(PublicacionDeInstagramRequest request, PublicacionDeInstagram publicacion) {
        publicacion.setUrl(normalizarUrl(request.url()));
        publicacion.setTipo(deducirTipo(request.tipo(), request.url()));
        publicacion.setTitulo(request.titulo());
        publicacion.setMiniaturaUrl(request.miniaturaUrl());
        publicacion.setMiniaturaPublicId(request.miniaturaPublicId());

        if (request.orden() != null) {
            publicacion.setOrden(request.orden());
        }
        if (request.activo() != null) {
            publicacion.setActivo(request.activo());
        }
    }

    /**
     * Instagram agrega parametros de seguimiento al copiar el link (igshid, utm).
     * Se recortan para que la misma publicacion no entre dos veces con URLs
     * distintas, y porque el embed no los necesita.
     */
    private String normalizarUrl(String url) {
        String sinParametros = url.split("\\?")[0].trim();
        return sinParametros.endsWith("/") ? sinParametros : sinParametros + "/";
    }

    /**
     * Si el panel no dice el tipo, se deduce del propio link: /reel/ y /reels/ son
     * reels, el resto posts. Asi la administradora solo tiene que pegar la URL.
     */
    private TipoDePublicacion deducirTipo(String tipoPedido, String url) {
        if (tipoPedido != null && !tipoPedido.isBlank()) {
            try {
                return TipoDePublicacion.valueOf(tipoPedido.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                // Un tipo mal escrito no justifica rechazar el guardado: la URL manda.
            }
        }
        String enMinusculas = url.toLowerCase();
        boolean esReel = enMinusculas.contains("/reel/") || enMinusculas.contains("/reels/");
        return esReel ? TipoDePublicacion.REEL : TipoDePublicacion.POST;
    }

    @Override
    public PublicacionDeInstagramResponse aResponse(PublicacionDeInstagram publicacion) {
        return new PublicacionDeInstagramResponse(
                publicacion.getId(),
                publicacion.getUrl(),
                publicacion.getTipo().name(),
                publicacion.getTitulo(),
                publicacion.getMiniaturaUrl(),
                publicacion.getMiniaturaPublicId(),
                publicacion.getOrden(),
                publicacion.getActivo(),
                publicacion.getActualizadoEn()
        );
    }

    @Override
    public PublicacionDeInstagramPublicaResponse aPublico(PublicacionDeInstagram publicacion) {
        return new PublicacionDeInstagramPublicaResponse(
                publicacion.getId(),
                publicacion.getUrl(),
                publicacion.getTipo().name(),
                publicacion.getTitulo(),
                publicacion.getMiniaturaUrl()
        );
    }
}
