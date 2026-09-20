package com.oriente.landing.mapper;

import com.oriente.landing.domain.InformacionDeContacto;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoRequest;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoResponse;
import com.oriente.landing.dto.publico.ContactoPublicoResponse;

public interface InformacionDeContactoMapper {

    void aplicar(InformacionDeContactoRequest request, InformacionDeContacto contacto);

    InformacionDeContactoResponse aResponse(InformacionDeContacto contacto);

    ContactoPublicoResponse aPublico(InformacionDeContacto contacto);
}
