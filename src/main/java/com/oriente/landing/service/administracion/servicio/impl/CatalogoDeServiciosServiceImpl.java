package com.oriente.landing.service.administracion.servicio.impl;

import com.oriente.landing.domain.Servicio;
import com.oriente.landing.dto.administracion.servicio.ServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ServicioResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.exception.ReglaDeNegocioException;
import com.oriente.landing.mapper.ServicioMapper;
import com.oriente.landing.repository.ServicioRepository;
import com.oriente.landing.service.administracion.imagen.BorradorDeImagenes;
import com.oriente.landing.service.administracion.imagen.ImagenesQuedaronHuerfanas;
import com.oriente.landing.service.administracion.servicio.CatalogoDeServiciosService;
import com.oriente.landing.util.GeneradorDeSlug;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CatalogoDeServiciosServiceImpl implements CatalogoDeServiciosService {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;
    private final ApplicationEventPublisher eventos;

    public CatalogoDeServiciosServiceImpl(ServicioRepository servicioRepository,
                                          ServicioMapper servicioMapper,
                                          ApplicationEventPublisher eventos) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
        this.eventos = eventos;
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

        // Las que estaban antes de tocar nada: las que no sobrevivan a la edicion
        // dejan de tener quien las nombre y hay que borrarlas del proveedor.
        Set<String> imagenesPrevias = imagenesDe(servicio);

        servicio.setSlug(resolverSlug(request, id));
        servicioMapper.aplicar(request, servicio);
        Servicio guardado = servicioRepository.save(servicio);

        eventos.publishEvent(ImagenesQuedaronHuerfanas.de(
                BorradorDeImagenes.loQueSobra(imagenesPrevias, imagenesDe(guardado))));

        return servicioMapper.aResponse(guardado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Servicio servicio = buscar(id);
        Set<String> imagenes = imagenesDe(servicio);
        Set<String> videos = new HashSet<>();
        servicio.getPublicaciones().forEach(publicacion -> {
            if (publicacion.getMiniaturaPublicId() != null) {
                imagenes.add(publicacion.getMiniaturaPublicId());
            }
            if (publicacion.getVideoPublicId() != null) {
                videos.add(publicacion.getVideoPublicId());
            }
        });

        servicioRepository.delete(servicio);
        eventos.publishEvent(ImagenesQuedaronHuerfanas.de(imagenes, videos));
    }

    /** La foto principal mas las del carrusel. */
    private Set<String> imagenesDe(Servicio servicio) {
        Set<String> publicIds = new HashSet<>();
        if (servicio.getImagenPublicId() != null) {
            publicIds.add(servicio.getImagenPublicId());
        }
        servicio.getImagenes().stream()
                .map(imagen -> imagen.getImagenPublicId())
                .filter(publicId -> publicId != null)
                .forEach(publicIds::add);
        return publicIds;
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
