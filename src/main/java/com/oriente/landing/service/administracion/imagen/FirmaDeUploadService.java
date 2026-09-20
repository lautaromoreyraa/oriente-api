package com.oriente.landing.service.administracion.imagen;

import com.oriente.landing.dto.administracion.imagen.FirmaDeUploadResponse;

public interface FirmaDeUploadService {

    /**
     * Firma un upload para que el panel pueda subir a Cloudinary sin conocer el
     * api_secret.
     */
    FirmaDeUploadResponse generarFirma();
}
