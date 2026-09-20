package com.oriente.landing.mapper;

import com.oriente.landing.domain.Nosotros;
import com.oriente.landing.dto.administracion.nosotros.NosotrosRequest;
import com.oriente.landing.dto.administracion.nosotros.NosotrosResponse;
import com.oriente.landing.dto.publico.NosotrosPublicoResponse;

public interface NosotrosMapper {

    void aplicar(NosotrosRequest request, Nosotros nosotros);

    NosotrosResponse aResponse(Nosotros nosotros);

    NosotrosPublicoResponse aPublico(Nosotros nosotros);
}
