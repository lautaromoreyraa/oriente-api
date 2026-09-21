package com.oriente.landing.service.administracion.imagen;

import java.util.Set;

/**
 * Avisa que unos archivos dejaron de estar referenciados.
 *
 * Se publica dentro de la transaccion pero se atiende despues del commit: si el
 * guardado termina fallando, los archivos no se tocan. Borrar primero y confiar en
 * que la transaccion va a salir bien deja archivos perdidos que ningun registro
 * vuelve a nombrar.
 *
 * Imagenes y videos van separados porque el proveedor los borra por caminos
 * distintos: pedirle que borre un video como si fuera una imagen no encuentra nada.
 */
public record ImagenesQuedaronHuerfanas(Set<String> imagenes, Set<String> videos) {

    public static ImagenesQuedaronHuerfanas de(Set<String> imagenes) {
        return new ImagenesQuedaronHuerfanas(imagenes, Set.of());
    }

    public static ImagenesQuedaronHuerfanas de(Set<String> imagenes, Set<String> videos) {
        return new ImagenesQuedaronHuerfanas(imagenes, videos);
    }

    public boolean estaVacio() {
        return imagenes.isEmpty() && videos.isEmpty();
    }
}
