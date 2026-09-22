package com.oriente.landing.service.administracion.instagram.impl;

import com.oriente.landing.domain.PublicacionDeInstagram;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;
import com.oriente.landing.enumeration.TipoDePublicacion;
import com.oriente.landing.exception.RecursoNoEncontradoException;
import com.oriente.landing.exception.ReglaDeNegocioException;
import com.oriente.landing.mapper.PublicacionDeInstagramMapper;
import com.oriente.landing.repository.PublicacionDeInstagramRepository;
import com.oriente.landing.service.administracion.imagen.BorradorDeImagenes;
import com.oriente.landing.service.administracion.imagen.ImagenesQuedaronHuerfanas;
import com.oriente.landing.service.administracion.imagen.SubidorDeArchivos;
import com.oriente.landing.service.administracion.instagram.FuenteDeInstagram;
import com.oriente.landing.service.administracion.instagram.PublicacionDeInstagramService;
import com.oriente.landing.service.administracion.instagram.ResolvedorDeEnlaces;
import com.oriente.landing.util.NormalizadorDeUrlDeInstagram;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import com.oriente.landing.domain.Servicio;
import com.oriente.landing.repository.ServicioRepository;

@Service
public class PublicacionDeInstagramServiceImpl implements PublicacionDeInstagramService {

    private final PublicacionDeInstagramRepository publicacionRepository;
    private final ServicioRepository servicioRepository;
    private final PublicacionDeInstagramMapper publicacionMapper;
    private final ResolvedorDeEnlaces resolvedor;
    private final ApplicationEventPublisher eventos;
    private final FuenteDeInstagram fuente;
    private final SubidorDeArchivos subidor;
    private final BorradorDeImagenes borrador;

    public PublicacionDeInstagramServiceImpl(
            PublicacionDeInstagramRepository publicacionRepository,
            ServicioRepository servicioRepository,
            PublicacionDeInstagramMapper publicacionMapper,
            ResolvedorDeEnlaces resolvedor,
            ApplicationEventPublisher eventos,
            FuenteDeInstagram fuente,
            SubidorDeArchivos subidor,
            BorradorDeImagenes borrador) {
        this.publicacionRepository = publicacionRepository;
        this.servicioRepository = servicioRepository;
        this.publicacionMapper = publicacionMapper;
        this.resolvedor = resolvedor;
        this.eventos = eventos;
        this.fuente = fuente;
        this.subidor = subidor;
        this.borrador = borrador;
    }

    /**
     * Los links del boton Compartir no dicen a que publicacion apuntan: se siguen
     * una vez, aca, y se guarda el destino. Si no se puede resolver, se sigue con
     * el link original.
     */
    private PublicacionDeInstagramRequest conElEnlaceResuelto(PublicacionDeInstagramRequest request) {
        if (!NormalizadorDeUrlDeInstagram.esEnlaceParaCompartir(request.url())) {
            return request;
        }

        return resolvedor.resolver(request.url())
                .map(resuelta -> new PublicacionDeInstagramRequest(
                        resuelta, request.tipo(), request.titulo(),
                        request.miniaturaUrl(), request.miniaturaPublicId(),
                        request.videoUrl(), request.videoPublicId(),
                        request.orden(), request.activo()))
                .orElse(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicacionDeInstagramResponse> listarDelServicio(Long servicioId) {
        return publicacionRepository.findAllByServicioIdOrderByOrdenAsc(servicioId).stream()
                .map(publicacionMapper::aResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PublicacionDeInstagramResponse obtenerPorId(Long id) {
        return publicacionMapper.aResponse(buscar(id));
    }

    /**
     * Sin @Transactional a proposito: tiene dos llamadas externas que pueden tardar
     * varios segundos, y no tiene sentido tener una conexion a la base tomada
     * mientras tanto. El unico acceso a la base que escribe es el save del final.
     */
    @Override
    public PublicacionDeInstagramResponse importar(Long servicioId, String url) {
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> RecursoNoEncontradoException.porId("Servicio", servicioId));

        String enlace = NormalizadorDeUrlDeInstagram.esEnlaceParaCompartir(url)
                ? resolvedor.resolver(url).orElseThrow(() -> new ReglaDeNegocioException(
                        "No se pudo abrir ese enlace para compartir. Copia el link desde la publicacion: "
                                + "los tres puntos, Copiar enlace"))
                : url;

        String codigo = NormalizadorDeUrlDeInstagram.codigo(enlace)
                .orElseThrow(() -> new ReglaDeNegocioException("Ese link no lleva a una publicacion"));

        FuenteDeInstagram.ContenidoDeInstagram contenido = fuente.buscar(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Esa publicacion no esta en la cuenta de Instagram del consultorio"));

        // Se chequea con el link que da Instagram y antes de subir nada: el mismo
        // contenido puede llegar como /p/ o como /reel/, y un repetido detectado
        // despues de subir deja archivos sueltos en Cloudinary.
        verificarQueNoEsteRepetida(contenido.enlace(), null);

        SubidorDeArchivos.ArchivoSubido video = contenido.esVideo()
                ? subidor.subirVideo(contenido.videoUrl())
                : null;
        SubidorDeArchivos.ArchivoSubido imagen;
        try {
            imagen = contenido.imagenUrl() == null ? null : subidor.subirImagen(contenido.imagenUrl());
        } catch (RuntimeException ex) {
            descartar(video, null);
            throw ex;
        }

        PublicacionDeInstagram publicacion = new PublicacionDeInstagram();
        publicacion.setServicio(servicio);
        publicacion.setUrl(contenido.enlace());
        publicacion.setTipo(contenido.esVideo() ? TipoDePublicacion.REEL : TipoDePublicacion.POST);
        publicacion.setVideoUrl(video == null ? null : video.url());
        publicacion.setVideoPublicId(video == null ? null : video.publicId());
        publicacion.setMiniaturaUrl(imagen == null ? null : imagen.url());
        publicacion.setMiniaturaPublicId(imagen == null ? null : imagen.publicId());
        // Al final de las del servicio: lo que ya estaba ordenado no se mueve.
        publicacion.setOrden((int) publicacionRepository.countByServicioId(servicioId));
        publicacion.setActivo(true);

        try {
            return publicacionMapper.aResponse(publicacionRepository.save(publicacion));
        } catch (RuntimeException ex) {
            descartar(video, imagen);
            throw ex;
        }
    }

    /** Si la publicacion no llega a guardarse, lo que se subio no lo referencia nadie. */
    private void descartar(SubidorDeArchivos.ArchivoSubido video, SubidorDeArchivos.ArchivoSubido imagen) {
        if (video != null) {
            borrador.borrarVideos(Set.of(video.publicId()));
        }
        if (imagen != null) {
            borrador.borrar(Set.of(imagen.publicId()));
        }
    }

    @Override
    @Transactional
    public PublicacionDeInstagramResponse actualizar(Long id, PublicacionDeInstagramRequest request) {
        PublicacionDeInstagram publicacion = buscar(id);
        Set<String> miniaturaPrevia = comoConjunto(publicacion.getMiniaturaPublicId());
        Set<String> videoPrevio = comoConjunto(publicacion.getVideoPublicId());

        publicacionMapper.aplicar(conElEnlaceResuelto(request), publicacion);
        verificarQueNoEsteRepetida(publicacion.getUrl(), id);
        PublicacionDeInstagram guardada = publicacionRepository.save(publicacion);

        eventos.publishEvent(ImagenesQuedaronHuerfanas.de(
                BorradorDeImagenes.loQueSobra(miniaturaPrevia, comoConjunto(guardada.getMiniaturaPublicId())),
                BorradorDeImagenes.loQueSobra(videoPrevio, comoConjunto(guardada.getVideoPublicId()))));

        return publicacionMapper.aResponse(guardada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        PublicacionDeInstagram publicacion = buscar(id);
        Set<String> miniatura = comoConjunto(publicacion.getMiniaturaPublicId());
        Set<String> video = comoConjunto(publicacion.getVideoPublicId());

        publicacionRepository.delete(publicacion);
        eventos.publishEvent(ImagenesQuedaronHuerfanas.de(miniatura, video));
    }

    private Set<String> comoConjunto(String publicId) {
        return publicId == null || publicId.isBlank() ? Set.of() : Set.of(publicId);
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
