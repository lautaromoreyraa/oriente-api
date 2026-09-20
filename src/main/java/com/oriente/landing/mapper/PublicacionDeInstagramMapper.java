package com.oriente.landing.mapper;

import com.oriente.landing.domain.PublicacionDeInstagram;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;
import com.oriente.landing.dto.publico.PublicacionDeInstagramPublicaResponse;

public interface PublicacionDeInstagramMapper {

    void aplicar(PublicacionDeInstagramRequest request, PublicacionDeInstagram publicacion);

    PublicacionDeInstagramResponse aResponse(PublicacionDeInstagram publicacion);

    PublicacionDeInstagramPublicaResponse aPublico(PublicacionDeInstagram publicacion);
}
