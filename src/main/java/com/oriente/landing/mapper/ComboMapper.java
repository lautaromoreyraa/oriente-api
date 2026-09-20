package com.oriente.landing.mapper;

import com.oriente.landing.domain.Combo;
import com.oriente.landing.dto.administracion.combo.ComboRequest;
import com.oriente.landing.dto.administracion.combo.ComboResponse;
import com.oriente.landing.dto.publico.ComboPublicoResponse;

public interface ComboMapper {

    void aplicar(ComboRequest request, Combo combo);

    ComboResponse aResponse(Combo combo);

    ComboPublicoResponse aPublico(Combo combo);
}
