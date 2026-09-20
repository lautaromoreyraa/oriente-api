package com.oriente.landing.service.administracion.instagram;

import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;

import java.util.List;

public interface PublicacionDeInstagramService {

    List<PublicacionDeInstagramResponse> listar();

    PublicacionDeInstagramResponse obtenerPorId(Long id);

    PublicacionDeInstagramResponse crear(PublicacionDeInstagramRequest request);

    PublicacionDeInstagramResponse actualizar(Long id, PublicacionDeInstagramRequest request);

    void eliminar(Long id);
}
