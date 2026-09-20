package com.oriente.landing.service.administracion.contacto;

import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoRequest;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoResponse;

public interface InformacionDeContactoService {

    InformacionDeContactoResponse obtener();

    InformacionDeContactoResponse guardar(InformacionDeContactoRequest request);
}
