package com.oriente.landing.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneradorDeSlugTest {

    @Test
    @DisplayName("saca los acentos en lugar de borrar la letra")
    void sacaLosAcentos() {
        assertEquals("depilacion-definitiva", GeneradorDeSlug.desde("Depilación Definitiva"));
        assertEquals("kinesiologia", GeneradorDeSlug.desde("Kinesiología"));
        assertEquals("masajes-y-relajacion", GeneradorDeSlug.desde("Masajes y Relajación"));
    }

    @Test
    @DisplayName("no deja guiones sueltos en los extremos ni repetidos en el medio")
    void limpiaLosGuiones() {
        assertEquals("detox-bienestar", GeneradorDeSlug.desde("  Detox & Bienestar  "));
        assertEquals("programa-piel", GeneradorDeSlug.desde("--Programa   Piel--"));
    }

    @Test
    @DisplayName("un texto sin letras ni numeros da un slug vacio, no un guion")
    void textoSinContenidoUtil() {
        assertEquals("", GeneradorDeSlug.desde("---"));
        assertEquals("", GeneradorDeSlug.desde("   "));
        assertEquals("", GeneradorDeSlug.desde(null));
    }
}
