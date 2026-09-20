package com.oriente.landing.dto.administracion.imagen;

/**
 * Lo que el panel necesita para subir un archivo a Cloudinary sin conocer el
 * api_secret.
 *
 * Antes el navegador subia con un upload preset sin firmar, con el preset a la
 * vista en el bundle: cualquiera podia subir archivos a la cuenta. Ahora la firma
 * la calcula el backend, vale para un solo upload y caduca.
 */
public record FirmaDeUploadResponse(
        String firma,
        Long marcaDeTiempo,
        String apiKey,
        String nombreDeLaNube,
        String carpeta
) {
}
