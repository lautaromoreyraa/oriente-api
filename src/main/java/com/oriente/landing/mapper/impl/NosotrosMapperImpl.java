package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.Diferencial;
import com.oriente.landing.domain.Estadistica;
import com.oriente.landing.domain.MiembroDelEquipo;
import com.oriente.landing.domain.Nosotros;
import com.oriente.landing.dto.administracion.nosotros.DiferencialRequest;
import com.oriente.landing.dto.administracion.nosotros.DiferencialResponse;
import com.oriente.landing.dto.administracion.nosotros.EstadisticaRequest;
import com.oriente.landing.dto.administracion.nosotros.EstadisticaResponse;
import com.oriente.landing.dto.administracion.nosotros.MiembroDelEquipoRequest;
import com.oriente.landing.dto.administracion.nosotros.MiembroDelEquipoResponse;
import com.oriente.landing.dto.administracion.nosotros.NosotrosRequest;
import com.oriente.landing.dto.administracion.nosotros.NosotrosResponse;
import com.oriente.landing.dto.publico.DiferencialPublicoResponse;
import com.oriente.landing.dto.publico.EstadisticaPublicaResponse;
import com.oriente.landing.dto.publico.MiembroPublicoResponse;
import com.oriente.landing.dto.publico.NosotrosPublicoResponse;
import com.oriente.landing.mapper.NosotrosMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NosotrosMapperImpl implements NosotrosMapper {

    @Override
    public void aplicar(NosotrosRequest request, Nosotros nosotros) {
        nosotros.setTitulo(request.titulo());
        nosotros.setCuerpo(request.cuerpo());
        nosotros.setImagenUrl(request.imagenUrl());
        nosotros.setImagenPublicId(request.imagenPublicId());
        nosotros.setImagenAlt(request.imagenAlt());

        if (request.activo() != null) {
            nosotros.setActivo(request.activo());
        }

        // Cada lista se reemplaza solo si vino en el request: mandar null significa
        // "no toques esta parte", mandar una lista vacia significa "vaciala".
        if (request.estadisticas() != null) {
            nosotros.vaciarEstadisticas();
            int posicion = 0;
            for (EstadisticaRequest estadisticaRequest : request.estadisticas()) {
                Estadistica estadistica = new Estadistica();
                estadistica.setValor(estadisticaRequest.valor());
                estadistica.setEtiqueta(estadisticaRequest.etiqueta());
                estadistica.setOrden(estadisticaRequest.orden() != null ? estadisticaRequest.orden() : posicion);
                estadistica.setActivo(estadisticaRequest.activo() != null ? estadisticaRequest.activo() : Boolean.TRUE);
                nosotros.agregarEstadistica(estadistica);
                posicion++;
            }
        }

        if (request.diferenciales() != null) {
            nosotros.vaciarDiferenciales();
            int posicion = 0;
            for (DiferencialRequest diferencialRequest : request.diferenciales()) {
                Diferencial diferencial = new Diferencial();
                diferencial.setTitulo(diferencialRequest.titulo());
                diferencial.setDescripcion(diferencialRequest.descripcion());
                diferencial.setOrden(diferencialRequest.orden() != null ? diferencialRequest.orden() : posicion);
                diferencial.setActivo(diferencialRequest.activo() != null ? diferencialRequest.activo() : Boolean.TRUE);
                nosotros.agregarDiferencial(diferencial);
                posicion++;
            }
        }

        if (request.equipo() != null) {
            nosotros.vaciarEquipo();
            int posicion = 0;
            for (MiembroDelEquipoRequest miembroRequest : request.equipo()) {
                MiembroDelEquipo miembro = new MiembroDelEquipo();
                miembro.setNombre(miembroRequest.nombre());
                miembro.setRol(miembroRequest.rol());
                miembro.setImagenUrl(miembroRequest.imagenUrl());
                miembro.setImagenPublicId(miembroRequest.imagenPublicId());
                miembro.setImagenAlt(miembroRequest.imagenAlt());
                miembro.setOrden(miembroRequest.orden() != null ? miembroRequest.orden() : posicion);
                miembro.setActivo(miembroRequest.activo() != null ? miembroRequest.activo() : Boolean.TRUE);
                nosotros.agregarMiembro(miembro);
                posicion++;
            }
        }
    }

    @Override
    public NosotrosResponse aResponse(Nosotros nosotros) {
        List<EstadisticaResponse> estadisticas = nosotros.getEstadisticas().stream()
                .map(estadistica -> new EstadisticaResponse(
                        estadistica.getId(),
                        estadistica.getValor(),
                        estadistica.getEtiqueta(),
                        estadistica.getOrden(),
                        estadistica.getActivo()))
                .toList();

        List<DiferencialResponse> diferenciales = nosotros.getDiferenciales().stream()
                .map(diferencial -> new DiferencialResponse(
                        diferencial.getId(),
                        diferencial.getTitulo(),
                        diferencial.getDescripcion(),
                        diferencial.getOrden(),
                        diferencial.getActivo()))
                .toList();

        List<MiembroDelEquipoResponse> equipo = nosotros.getEquipo().stream()
                .map(miembro -> new MiembroDelEquipoResponse(
                        miembro.getId(),
                        miembro.getNombre(),
                        miembro.getRol(),
                        miembro.getImagenUrl(),
                        miembro.getImagenPublicId(),
                        miembro.getImagenAlt(),
                        miembro.getOrden(),
                        miembro.getActivo()))
                .toList();

        return new NosotrosResponse(
                nosotros.getId(),
                nosotros.getTitulo(),
                nosotros.getCuerpo(),
                nosotros.getImagenUrl(),
                nosotros.getImagenPublicId(),
                nosotros.getImagenAlt(),
                nosotros.getActivo(),
                estadisticas,
                diferenciales,
                equipo,
                nosotros.getActualizadoEn()
        );
    }

    @Override
    public NosotrosPublicoResponse aPublico(Nosotros nosotros) {
        List<EstadisticaPublicaResponse> estadisticas = nosotros.getEstadisticas().stream()
                .filter(estadistica -> Boolean.TRUE.equals(estadistica.getActivo()))
                .map(estadistica -> new EstadisticaPublicaResponse(estadistica.getValor(), estadistica.getEtiqueta()))
                .toList();

        List<DiferencialPublicoResponse> diferenciales = nosotros.getDiferenciales().stream()
                .filter(diferencial -> Boolean.TRUE.equals(diferencial.getActivo()))
                .map(diferencial -> new DiferencialPublicoResponse(diferencial.getTitulo(), diferencial.getDescripcion()))
                .toList();

        List<MiembroPublicoResponse> equipo = nosotros.getEquipo().stream()
                .filter(miembro -> Boolean.TRUE.equals(miembro.getActivo()))
                .map(miembro -> new MiembroPublicoResponse(
                        miembro.getNombre(),
                        miembro.getRol(),
                        miembro.getImagenUrl(),
                        miembro.getImagenAlt()))
                .toList();

        return new NosotrosPublicoResponse(
                nosotros.getTitulo(),
                nosotros.getCuerpo(),
                nosotros.getImagenUrl(),
                nosotros.getImagenAlt(),
                estadisticas,
                diferenciales,
                equipo
        );
    }
}
