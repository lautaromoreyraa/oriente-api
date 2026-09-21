package com.oriente.landing.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Instagram entrega el mismo contenido bajo varias formas segun de donde se copie
 * el link. La validacion anterior solo aceptaba la de la web, que justamente no es
 * la que da el telefono: desde un perfil en la app sale /usuario/p/CODIGO y desde
 * el boton Compartir sale /share/ALGO, y las dos quedaban afuera.
 */
class NormalizadorDeUrlDeInstagramTest {

    private String normalizada(String url) {
        return NormalizadorDeUrlDeInstagram.normalizar(url).orElse(null);
    }

    @Test
    @DisplayName("el link con el usuario en el medio es la misma publicacion que el corto")
    void quitaElUsuarioDelMedio() {
        assertEquals("https://www.instagram.com/p/ABC123/",
                normalizada("https://www.instagram.com/oriente.rcia/p/ABC123/"));
        assertEquals("https://www.instagram.com/reel/ABC123/",
                normalizada("https://www.instagram.com/oriente.rcia/reel/ABC123/"));
    }

    @Test
    @DisplayName("recorta los parametros de seguimiento que Instagram agrega al copiar")
    void recortaLosParametros() {
        assertEquals("https://www.instagram.com/p/ABC123/",
                normalizada("https://www.instagram.com/p/ABC123/?igshid=xyz&img_index=1"));
    }

    @Test
    @DisplayName("reels y reel llevan al mismo lugar y se guardan igual")
    void unificaReelEnSingular() {
        assertEquals("https://www.instagram.com/reel/ABC123/",
                normalizada("https://www.instagram.com/reels/ABC123/"));
        assertEquals("https://www.instagram.com/reel/ABC123/",
                normalizada("https://www.instagram.com/reel/ABC123/"));
    }

    @Test
    @DisplayName("acepta http, sin www y el dominio corto")
    void toleraLasVariantesDelDominio() {
        String esperada = "https://www.instagram.com/p/ABC123/";
        assertEquals(esperada, normalizada("http://www.instagram.com/p/ABC123/"));
        assertEquals(esperada, normalizada("https://instagram.com/p/ABC123/"));
        assertEquals(esperada, normalizada("https://instagr.am/p/ABC123/"));
    }

    @Test
    @DisplayName("agrega la barra final que falta")
    void cierraConBarra() {
        assertEquals("https://www.instagram.com/p/ABC123/",
                normalizada("https://www.instagram.com/p/ABC123"));
    }

    @Test
    @DisplayName("un enlace para compartir se reconoce, pero todavia no se sabe a que apunta")
    void elEnlaceParaCompartirNoSeNormalizaSolo() {
        String paraCompartir = "https://www.instagram.com/share/BAbcDefGhI";

        assertTrue(NormalizadorDeUrlDeInstagram.esEnlaceParaCompartir(paraCompartir));
        assertTrue(NormalizadorDeUrlDeInstagram.pareceDeInstagram(paraCompartir));
        // Hay que seguirlo para saber la publicacion: eso lo hace el resolvedor.
        assertEquals(null, normalizada(paraCompartir));
    }

    @Test
    @DisplayName("lo que no es una publicacion queda afuera")
    void rechazaLoQueNoEsUnaPublicacion() {
        assertFalse(NormalizadorDeUrlDeInstagram.pareceDeInstagram("https://tiktok.com/@oriente/video/1"));
        assertFalse(NormalizadorDeUrlDeInstagram.pareceDeInstagram("https://www.instagram.com/oriente.rcia/"));
        assertFalse(NormalizadorDeUrlDeInstagram.pareceDeInstagram("https://www.instagram.com/"));
        assertFalse(NormalizadorDeUrlDeInstagram.pareceDeInstagram(null));
        assertFalse(NormalizadorDeUrlDeInstagram.pareceDeInstagram("   "));
    }

    @Test
    @DisplayName("distingue los reels de las publicaciones comunes")
    void reconoceLosReels() {
        assertTrue(NormalizadorDeUrlDeInstagram.esReel("https://www.instagram.com/reel/ABC/"));
        assertTrue(NormalizadorDeUrlDeInstagram.esReel("https://www.instagram.com/reels/ABC/"));
        assertTrue(NormalizadorDeUrlDeInstagram.esReel("https://www.instagram.com/tv/ABC/"));
        assertFalse(NormalizadorDeUrlDeInstagram.esReel("https://www.instagram.com/p/ABC/"));
    }

    @Test
    @DisplayName("el codigo es el mismo en un post, un reel y un link copiado desde un perfil")
    void extraeElCodigo() {
        assertEquals(Optional.of("DdRYeNvxV8J"),
                NormalizadorDeUrlDeInstagram.codigo("https://www.instagram.com/p/DdRYeNvxV8J/"));
        assertEquals(Optional.of("DdRYeNvxV8J"),
                NormalizadorDeUrlDeInstagram.codigo("https://www.instagram.com/reel/DdRYeNvxV8J/?igsh=abc"));
        assertEquals(Optional.of("DdRYeNvxV8J"),
                NormalizadorDeUrlDeInstagram.codigo("https://instagram.com/oriente.rcia/reel/DdRYeNvxV8J"));
        assertTrue(NormalizadorDeUrlDeInstagram.codigo("https://www.instagram.com/share/BAabc123").isEmpty());
    }
}
