package com.oriente.landing.service.administracion.instagram;

import java.util.Optional;

/** Las publicaciones de la cuenta de Instagram del consultorio. */
public interface FuenteDeInstagram {

    /**
     * Busca una publicacion de la cuenta por su codigo (lo que va despues de /p/ o
     * /reel/ en el link). Vacio si la cuenta no tiene ninguna con ese codigo.
     */
    Optional<ContenidoDeInstagram> buscar(String codigo);

    /**
     * Lo que se necesita de una publicacion para mostrarla en la landing.
     *
     * @param enlace     el link canonico que da Instagram
     * @param videoUrl   el archivo del video; nulo si la publicacion es una foto
     * @param imagenUrl  la portada del video, o la foto
     */
    record ContenidoDeInstagram(String enlace, String videoUrl, String imagenUrl) {

        public boolean esVideo() {
            return videoUrl != null && !videoUrl.isBlank();
        }
    }
}
