package com.oriente.landing.service.administracion.instagram;

import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;

import java.util.List;

public interface PublicacionDeInstagramService {

    List<PublicacionDeInstagramResponse> listarDelServicio(Long servicioId);

    PublicacionDeInstagramResponse obtenerPorId(Long id);

    /**
     * Agrega una publicacion a un servicio a partir de su link: el video y la
     * portada se traen de la cuenta de Instagram y se guardan en Cloudinary.
     */
    PublicacionDeInstagramResponse importar(Long servicioId, String url);

    PublicacionDeInstagramResponse actualizar(Long id, PublicacionDeInstagramRequest request);

    void eliminar(Long id);
}
