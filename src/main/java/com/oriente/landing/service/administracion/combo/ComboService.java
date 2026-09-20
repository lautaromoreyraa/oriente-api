package com.oriente.landing.service.administracion.combo;

import com.oriente.landing.dto.administracion.combo.ComboRequest;
import com.oriente.landing.dto.administracion.combo.ComboResponse;

import java.util.List;

public interface ComboService {

    List<ComboResponse> listar();

    ComboResponse obtenerPorId(Long id);

    ComboResponse crear(ComboRequest request);

    ComboResponse actualizar(Long id, ComboRequest request);

    void eliminar(Long id);
}
