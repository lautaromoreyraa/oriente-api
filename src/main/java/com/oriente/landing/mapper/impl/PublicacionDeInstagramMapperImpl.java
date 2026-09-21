package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.PublicacionDeInstagram;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;
import com.oriente.landing.dto.publico.PublicacionDeInstagramPublicaResponse;
import com.oriente.landing.enumeration.TipoDePublicacion;
import com.oriente.landing.mapper.PublicacionDeInstagramMapper;
import com.oriente.landing.util.NormalizadorDeUrlDeInstagram;
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
        publicacion.setVideoUrl(request.videoUrl());
        publicacion.setVideoPublicId(request.videoPublicId());

        if (request.orden() != null) {
            publicacion.setOrden(request.orden());
        }
        if (request.activo() != null) {
            publicacion.setActivo(request.activo());
        }
    }

    /**
     * Lleva el link a su forma canonica: sin los parametros de seguimiento que
     * Instagram agrega al copiar, sin el usuario en el medio y con el dominio
     * unificado. Asi el mismo post copiado desde la web y desde el telefono se
     * guarda igual, y el chequeo de repetidos lo reconoce.
     *
     * Un link que el normalizador no reconoce se guarda como vino: la validacion
     * ya lo dejo pasar, y perder el dato seria peor que guardarlo sin normalizar.
     */
    private String normalizarUrl(String url) {
        return NormalizadorDeUrlDeInstagram.normalizar(url)
                .orElseGet(() -> url.split("\\?")[0].trim());
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
        return NormalizadorDeUrlDeInstagram.esReel(url) ? TipoDePublicacion.REEL : TipoDePublicacion.POST;
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
                publicacion.getVideoUrl(),
                publicacion.getVideoPublicId(),
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
                publicacion.getMiniaturaUrl(),
                publicacion.getVideoUrl()
        );
    }
}
