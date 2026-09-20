package com.oriente.landing.service.administracion.servicio.impl;

import com.oriente.landing.domain.Servicio;
import com.oriente.landing.dto.administracion.servicio.ServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ServicioResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.exception.ReglaDeNegocioException;
import com.oriente.landing.mapper.ServicioMapper;
import com.oriente.landing.repository.ServicioRepository;
import com.oriente.landing.service.administracion.servicio.CatalogoDeServiciosService;
import com.oriente.landing.util.GeneradorDeSlug;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogoDeServiciosServiceImpl implements CatalogoDeServiciosService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;

    public CatalogoDeServiciosServiceImpl(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listar() {
        return servicioRepository.findAllByOrderByOrdenAsc().stream()
                .map(servicioMapper::aResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponse obtenerPorId(Long id) {
        return servicioMapper.aResponse(buscar(id));
    }

    @Override
    @Transactional
    public ServicioResponse crear(ServicioRequest request) {
        Servicio servicio = new Servicio();
        servicio.setSlug(resolverSlug(request, null));
        servicioMapper.aplicar(request, servicio);
        return servicioMapper.aResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public ServicioResponse actualizar(Long id, ServicioRequest request) {
        Servicio servicio = buscar(id);
        servicio.setSlug(resolverSlug(request, id));
        servicioMapper.aplicar(request, servicio);
        return servicioMapper.aResponse(servicioRepository.save(servicio));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        servicioRepository.delete(buscar(id));
    }

    private Servicio buscar(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.porId("Servicio", id));
    }

    /**
     * El slug se deriva del titulo cuando el panel no lo manda, y se verifica que
     * no choque con otro servicio. La unicidad tambien esta en la base; el chequeo
     * previo existe para devolver un 409 con un mensaje util en lugar de un error
     * de integridad.
     */
    private String resolverSlug(ServicioRequest request, Long idActual) {
        String slug = (request.slug() == null || request.slug().isBlank())
                ? GeneradorDeSlug.desde(request.titulo())
                : GeneradorDeSlug.desde(request.slug());

        if (slug.isBlank()) {
            throw new ReglaDeNegocioException("No se pudo derivar un slug del titulo");
        }

        boolean repetido = (idActual == null)
                ? servicioRepository.existsBySlug(slug)
                : servicioRepository.existsBySlugAndIdNot(slug, idActual);

        if (repetido) {
            throw new ReglaDeNegocioException("Ya existe un servicio con el slug '" + slug + "'");
        }

        return slug;
    }
}
