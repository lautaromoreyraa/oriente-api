package com.oriente.landing.service.administracion.imagen;

import java.util.Set;

/**
 * Avisa que unas imagenes dejaron de estar referenciadas.
 *
 * Se publica dentro de la transaccion pero se atiende despues del commit: si el
 * guardado termina fallando, los archivos no se tocan. Borrar primero y confiar en
 * que la transaccion va a salir bien deja imagenes perdidas que ningun registro
 * vuelve a nombrar.
 */
public record ImagenesQuedaronHuerfanas(Set<String> publicIds) {

    public static ImagenesQuedaronHuerfanas de(Set<String> publicIds) {
        return new ImagenesQuedaronHuerfanas(publicIds);
    }
}
