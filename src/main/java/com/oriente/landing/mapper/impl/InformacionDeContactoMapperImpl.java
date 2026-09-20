package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.InformacionDeContacto;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoRequest;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoResponse;
import com.oriente.landing.dto.publico.ContactoPublicoResponse;
import com.oriente.landing.mapper.InformacionDeContactoMapper;
import org.springframework.stereotype.Component;

@Component
public class InformacionDeContactoMapperImpl implements InformacionDeContactoMapper {

    @Override
    public void aplicar(InformacionDeContactoRequest request, InformacionDeContacto contacto) {
        contacto.setWhatsapp(request.whatsapp());
        contacto.setWhatsappParaMostrar(request.whatsappParaMostrar());
        contacto.setInstagram(request.instagram());
        contacto.setDireccion(request.direccion());
        contacto.setUrlDeMaps(request.urlDeMaps());
        contacto.setEmbedDeMaps(request.embedDeMaps());
    }

    @Override
    public InformacionDeContactoResponse aResponse(InformacionDeContacto contacto) {
        return new InformacionDeContactoResponse(
                contacto.getId(),
                contacto.getWhatsapp(),
                contacto.getWhatsappParaMostrar(),
                contacto.getInstagram(),
                contacto.getDireccion(),
                contacto.getUrlDeMaps(),
                contacto.getEmbedDeMaps(),
                contacto.getActualizadoEn()
        );
    }

    @Override
    public ContactoPublicoResponse aPublico(InformacionDeContacto contacto) {
        return new ContactoPublicoResponse(
                contacto.getWhatsapp(),
                contacto.getWhatsappParaMostrar(),
                contacto.getInstagram(),
                contacto.getDireccion(),
                contacto.getUrlDeMaps(),
                contacto.getEmbedDeMaps()
        );
    }
}
