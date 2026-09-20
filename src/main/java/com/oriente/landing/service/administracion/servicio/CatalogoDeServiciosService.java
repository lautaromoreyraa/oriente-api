package com.oriente.landing.service.administracion.servicio;

import com.oriente.landing.dto.administracion.servicio.ServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ServicioResponse;

import java.util.List;

/**
 * Administracion del catalogo de servicios.
 *
 * Se llama catalogo y no "ServicioService" porque la entidad ya se llama
 * Servicio: repetir la palabra no agrega informacion y choca con el @Service de
 * Spring al leer el codigo.
 */
public interface CatalogoDeServiciosService {

    /** Incluye los desactivados: el panel tiene que poder volver a encenderlos. */
    List<ServicioResponse> listar();

    ServicioResponse obtenerPorId(Long id);

    ServicioResponse crear(ServicioRequest request);

    ServicioResponse actualizar(Long id, ServicioRequest request);

    void eliminar(Long id);
}
