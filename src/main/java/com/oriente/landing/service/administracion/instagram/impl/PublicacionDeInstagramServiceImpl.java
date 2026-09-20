package com.oriente.landing.service.administracion.instagram.impl;

import com.oriente.landing.domain.PublicacionDeInstagram;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.exception.ReglaDeNegocioException;
import com.oriente.landing.mapper.PublicacionDeInstagramMapper;
import com.oriente.landing.repository.PublicacionDeInstagramRepository;
import com.oriente.landing.service.administracion.imagen.BorradorDeImagenes;
import com.oriente.landing.service.administracion.imagen.ImagenesQuedaronHuerfanas;
import com.oriente.landing.service.administracion.instagram.PublicacionDeInstagramService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PublicacionDeInstagramServiceImpl implements PublicacionDeInstagramService {

    private final PublicacionDeInstagramRepository publicacionRepository;
    private final PublicacionDeInstagramMapper publicacionMapper;
    private final ApplicationEventPublisher eventos;

    public PublicacionDeInstagramServiceImpl(
            PublicacionDeInstagramRepository publicacionRepository,
            PublicacionDeInstagramMapper publicacionMapper,
            ApplicationEventPublisher eventos) {
        this.publicacionRepository = publicacionRepository;
        this.publicacionMapper = publicacionMapper;
        this.eventos = eventos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicacionDeInstagramResponse> listar() {
        return publicacionRepository.findAllByOrderByOrdenAsc().stream()
                .map(publicacionMapper::aResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PublicacionDeInstagramResponse obtenerPorId(Long id) {
        return publicacionMapper.aResponse(buscar(id));
    }

    @Override
    @Transactional
    public PublicacionDeInstagramResponse crear(PublicacionDeInstagramRequest request) {
        PublicacionDeInstagram publicacion = new PublicacionDeInstagram();
        publicacionMapper.aplicar(request, publicacion);
        verificarQueNoEsteRepetida(publicacion.getUrl(), null);
        return publicacionMapper.aResponse(publicacionRepository.save(publicacion));
    }

    @Override
    @Transactional
    public PublicacionDeInstagramResponse actualizar(Long id, PublicacionDeInstagramRequest request) {
        PublicacionDeInstagram publicacion = buscar(id);
        String miniaturaPrevia = publicacion.getMiniaturaPublicId();

        publicacionMapper.aplicar(request, publicacion);
        verificarQueNoEsteRepetida(publicacion.getUrl(), id);
        PublicacionDeInstagram guardada = publicacionRepository.save(publicacion);

        eventos.publishEvent(ImagenesQuedaronHuerfanas.de(BorradorDeImagenes.loQueSobra(
                miniaturaPrevia == null ? Set.of() : Set.of(miniaturaPrevia),
                guardada.getMiniaturaPublicId() == null ? Set.of() : Set.of(guardada.getMiniaturaPublicId()))));

        return publicacionMapper.aResponse(guardada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        PublicacionDeInstagram publicacion = buscar(id);
        String miniatura = publicacion.getMiniaturaPublicId();
        publicacionRepository.delete(publicacion);

        if (miniatura != null) {
            eventos.publishEvent(ImagenesQuedaronHuerfanas.de(Set.of(miniatura)));
        }
    }

    private PublicacionDeInstagram buscar(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> RecursoNoEncontradoException.porId("Publicacion de Instagram", id));
    }

    /**
     * La URL ya viene normalizada por el mapper, asi que el mismo post pegado dos
     * veces con distintos parametros de seguimiento se detecta como repetido.
     */
    private void verificarQueNoEsteRepetida(String url, Long idActual) {
        boolean repetida = (idActual == null)
                ? publicacionRepository.existsByUrl(url)
                : publicacionRepository.existsByUrlAndIdNot(url, idActual);

        if (repetida) {
            throw new ReglaDeNegocioException("Esa publicacion ya esta en la lista");
        }
    }
}
