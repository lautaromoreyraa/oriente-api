package com.oriente.landing.service.administracion.imagen;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BorradorDeImagenesTest {

    @Test
    @DisplayName("sobra lo que estaba antes y ya no esta despues")
    void calculaLaDiferencia() {
        Set<String> huerfanas = BorradorDeImagenes.loQueSobra(
                List.of("oriente/a", "oriente/b", "oriente/c"),
                List.of("oriente/b"));

        assertEquals(Set.of("oriente/a", "oriente/c"), huerfanas);
    }

    @Test
    @DisplayName("lo que sigue referenciado no se borra")
    void noBorraLoQueSigueEnUso() {
        assertTrue(BorradorDeImagenes.loQueSobra(
                List.of("oriente/a"),
                List.of("oriente/a", "oriente/b")).isEmpty());
    }

    @Test
    @DisplayName("descarta los nulos y los vacios en lugar de pedirle a Cloudinary que borre nada")
    void descartaValoresSinContenido() {
        Set<String> huerfanas = BorradorDeImagenes.loQueSobra(
                Arrays.asList("oriente/a", null, "", "   "),
                List.of());

        assertEquals(Set.of("oriente/a"), huerfanas);
    }
}
