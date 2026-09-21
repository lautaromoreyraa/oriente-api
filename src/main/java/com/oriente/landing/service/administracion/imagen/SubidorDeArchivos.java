package com.oriente.landing.service.administracion.imagen;

/**
 * Sube a Cloudinary un archivo que ya esta publicado en otra URL.
 *
 * No pasa por el servidor: Cloudinary lo baja directo desde el origen. Lo que
 * se usa en la landing es la copia, porque las URLs de origen (las del CDN de
 * Instagram) vencen a las pocas horas.
 */
public interface SubidorDeArchivos {

    ArchivoSubido subirImagen(String urlDeOrigen);

    ArchivoSubido subirVideo(String urlDeOrigen);

    record ArchivoSubido(String url, String publicId) {
    }
}
