package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.ImagenDeServicio;
import com.oriente.landing.domain.Servicio;
import com.oriente.landing.dto.administracion.servicio.ImagenDeServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ImagenDeServicioResponse;
import com.oriente.landing.dto.administracion.servicio.ServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ServicioResponse;
import com.oriente.landing.dto.publico.ImagenPublicaResponse;
import com.oriente.landing.dto.publico.ServicioPublicoResponse;
import com.oriente.landing.enumeration.CategoriaDeServicio;
import com.oriente.landing.exception.ReglaDeNegocioException;
import com.oriente.landing.mapper.ServicioMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServicioMapperImpl implements ServicioMapper {

    @Override
    public void aplicar(ServicioRequest request, Servicio servicio) {
        servicio.setCategoria(aCategoria(request.categoria()));
        servicio.setTitulo(request.titulo());
        servicio.setDescripcion(request.descripcion());
        servicio.setDescripcionLarga(request.descripcionLarga());
        servicio.setImagenUrl(request.imagenUrl());
        servicio.setImagenPublicId(request.imagenPublicId());
        servicio.setImagenAlt(request.imagenAlt());

        if (request.orden() != null) {
            servicio.setOrden(request.orden());
        }
        if (request.activo() != null) {
            servicio.setActivo(request.activo());
        }

        aplicarImagenes(request, servicio);
    }

    /**
     * El carrusel se reemplaza entero.
     *
     * El panel manda la lista completa en el orden en que quedo despues de
     * arrastrar: reconciliar imagen por imagen daria el mismo resultado con mas
     * codigo, y orphanRemoval se encarga de borrar las que ya no estan.
     */
    private void aplicarImagenes(ServicioRequest request, Servicio servicio) {
        if (request.imagenes() == null) {
            return;
        }

        servicio.vaciarImagenes();

        int posicion = 0;
        for (ImagenDeServicioRequest imagenRequest : request.imagenes()) {
            ImagenDeServicio imagen = new ImagenDeServicio();
            imagen.setImagenUrl(imagenRequest.imagenUrl());
            imagen.setImagenPublicId(imagenRequest.imagenPublicId());
            imagen.setAlt(imagenRequest.alt());
            imagen.setUrlDePublicacion(imagenRequest.urlDePublicacion());
            // Si el panel no manda orden, gana la posicion en la lista.
            imagen.setOrden(imagenRequest.orden() != null ? imagenRequest.orden() : posicion);
            imagen.setActivo(imagenRequest.activo() != null ? imagenRequest.activo() : Boolean.TRUE);
            servicio.agregarImagen(imagen);
            posicion++;
        }
    }

    @Override
    public ServicioResponse aResponse(Servicio servicio) {
        List<ImagenDeServicioResponse> imagenes = servicio.getImagenes().stream()
                .map(imagen -> new ImagenDeServicioResponse(
                        imagen.getId(),
                        imagen.getImagenUrl(),
                        imagen.getImagenPublicId(),
                        imagen.getAlt(),
                        imagen.getUrlDePublicacion(),
                        imagen.getOrden(),
                        imagen.getActivo()))
                .toList();

        return new ServicioResponse(
                servicio.getId(),
                servicio.getSlug(),
                servicio.getCategoria().name(),
                servicio.getTitulo(),
                servicio.getDescripcion(),
                servicio.getDescripcionLarga(),
                servicio.getImagenUrl(),
                servicio.getImagenPublicId(),
                servicio.getImagenAlt(),
                servicio.getOrden(),
                servicio.getActivo(),
                imagenes,
                servicio.getActualizadoEn()
        );
    }

    @Override
    public ServicioPublicoResponse aPublico(Servicio servicio) {
        // Las imagenes desactivadas no salen a la landing, aunque el panel las siga
        // viendo para poder reactivarlas.
        List<ImagenPublicaResponse> imagenes = servicio.getImagenes().stream()
                .filter(imagen -> Boolean.TRUE.equals(imagen.getActivo()))
                .map(imagen -> new ImagenPublicaResponse(
                        imagen.getImagenUrl(),
                        imagen.getAlt(),
                        imagen.getUrlDePublicacion()))
                .toList();

        return new ServicioPublicoResponse(
                servicio.getId(),
                servicio.getSlug(),
                servicio.getCategoria().name(),
                servicio.getTitulo(),
                servicio.getDescripcion(),
                servicio.getDescripcionLarga(),
                servicio.getImagenUrl(),
                servicio.getImagenAlt(),
                servicio.getOrden(),
                imagenes
        );
    }

    private CategoriaDeServicio aCategoria(String valor) {
        try {
            return CategoriaDeServicio.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new ReglaDeNegocioException(
                    "Categoria invalida: '" + valor + "'. Los valores validos son KINE y ESTETICA");
        }
    }
}
