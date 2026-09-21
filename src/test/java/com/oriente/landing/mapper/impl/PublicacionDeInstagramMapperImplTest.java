package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.PublicacionDeInstagram;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.enumeration.TipoDePublicacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PublicacionDeInstagramMapperImplTest {

    private final PublicacionDeInstagramMapperImpl mapper = new PublicacionDeInstagramMapperImpl();

    private PublicacionDeInstagram aplicar(String url, String tipo) {
        PublicacionDeInstagram publicacion = new PublicacionDeInstagram();
        mapper.aplicar(new PublicacionDeInstagramRequest(url, tipo, null, null, null, null, null, null, null), publicacion);
        return publicacion;
    }

    @Test
    @DisplayName("recorta los parametros de seguimiento que Instagram agrega al copiar el link")
    void recortaLosParametros() {
        assertEquals("https://www.instagram.com/p/ABC123/",
                aplicar("https://www.instagram.com/p/ABC123/?igshid=xyz&utm_source=ig_web", null).getUrl());
    }

    @Test
    @DisplayName("cierra la URL con barra para que el mismo post no entre dos veces")
    void normalizaLaBarraFinal() {
        assertEquals("https://www.instagram.com/p/ABC123/",
                aplicar("https://www.instagram.com/p/ABC123", null).getUrl());
    }

    @Test
    @DisplayName("deduce que es un reel mirando el link")
    void deduceElTipoDesdeLaUrl() {
        assertEquals(TipoDePublicacion.REEL, aplicar("https://www.instagram.com/reel/ABC123/", null).getTipo());
        assertEquals(TipoDePublicacion.REEL, aplicar("https://www.instagram.com/reels/ABC123/", null).getTipo());
        assertEquals(TipoDePublicacion.POST, aplicar("https://www.instagram.com/p/ABC123/", null).getTipo());
    }

    @Test
    @DisplayName("un tipo mal escrito no tira el guardado: manda la URL")
    void tipoInvalidoCaeEnLaDeduccion() {
        assertEquals(TipoDePublicacion.REEL, aplicar("https://www.instagram.com/reel/ABC123/", "vidrio").getTipo());
    }

    @Test
    @DisplayName("si el panel dice el tipo explicitamente, se respeta")
    void respetaElTipoPedido() {
        assertEquals(TipoDePublicacion.REEL, aplicar("https://www.instagram.com/p/ABC123/", "REEL").getTipo());
    }
}
