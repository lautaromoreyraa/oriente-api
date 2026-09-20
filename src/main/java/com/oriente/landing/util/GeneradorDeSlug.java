package com.oriente.landing.util;

import java.text.Normalizer;
import java.util.Locale;

/** Convierte un titulo en un slug apto para una URL. Sin estado. */
public final class GeneradorDeSlug {

    private GeneradorDeSlug() {
    }

    public static String desde(String texto) {
        if (texto == null || texto.isBlank()) {
            return "";
        }

        // Se separan los acentos del caracter base y se descartan, para que
        // "Depilación" quede "depilacion" y no "depilacin".
        String sinAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return sinAcentos.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
