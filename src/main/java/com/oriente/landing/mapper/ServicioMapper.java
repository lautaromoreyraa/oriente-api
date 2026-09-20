package com.oriente.landing.mapper;

import com.oriente.landing.domain.Servicio;
import com.oriente.landing.dto.administracion.servicio.ServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ServicioResponse;
import com.oriente.landing.dto.publico.ServicioPublicoResponse;

public interface ServicioMapper {

    /**
     * Copia los campos editables y reemplaza el carrusel completo por el que trae
     * el request. El slug lo resuelve el service, que es quien sabe si ya existe.
     */
    void aplicar(ServicioRequest request, Servicio servicio);

    ServicioResponse aResponse(Servicio servicio);

    ServicioPublicoResponse aPublico(Servicio servicio);
}
