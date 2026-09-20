package com.oriente.landing.mapper.impl;

import com.oriente.landing.domain.Combo;
import com.oriente.landing.domain.ItemDeCombo;
import com.oriente.landing.dto.administracion.combo.ComboRequest;
import com.oriente.landing.dto.administracion.combo.ComboResponse;
import com.oriente.landing.dto.administracion.combo.ItemDeComboRequest;
import com.oriente.landing.dto.administracion.combo.ItemDeComboResponse;
import com.oriente.landing.dto.publico.ComboPublicoResponse;
import com.oriente.landing.mapper.ComboMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComboMapperImpl implements ComboMapper {

    @Override
    public void aplicar(ComboRequest request, Combo combo) {
        combo.setTitulo(request.titulo());
        combo.setBajada(request.bajada());
        combo.setDescripcion(request.descripcion());
        combo.setEtiqueta(request.etiqueta());

        if (request.orden() != null) {
            combo.setOrden(request.orden());
        }
        if (request.activo() != null) {
            combo.setActivo(request.activo());
        }

        if (request.items() != null) {
            combo.vaciarItems();
            int posicion = 0;
            for (ItemDeComboRequest itemRequest : request.items()) {
                ItemDeCombo item = new ItemDeCombo();
                item.setDescripcion(itemRequest.descripcion());
                item.setOrden(itemRequest.orden() != null ? itemRequest.orden() : posicion);
                combo.agregarItem(item);
                posicion++;
            }
        }
    }

    @Override
    public ComboResponse aResponse(Combo combo) {
        List<ItemDeComboResponse> items = combo.getItems().stream()
                .map(item -> new ItemDeComboResponse(item.getId(), item.getDescripcion(), item.getOrden()))
                .toList();

        return new ComboResponse(
                combo.getId(),
                combo.getSlug(),
                combo.getTitulo(),
                combo.getBajada(),
                combo.getDescripcion(),
                combo.getEtiqueta(),
                combo.getOrden(),
                combo.getActivo(),
                items,
                combo.getActualizadoEn()
        );
    }

    @Override
    public ComboPublicoResponse aPublico(Combo combo) {
        // La landing solo lista los textos de lo que incluye el combo: los ids de
        // los items no le sirven de nada.
        List<String> incluye = combo.getItems().stream()
                .map(ItemDeCombo::getDescripcion)
                .toList();

        return new ComboPublicoResponse(
                combo.getId(),
                combo.getSlug(),
                combo.getTitulo(),
                combo.getBajada(),
                combo.getDescripcion(),
                combo.getEtiqueta(),
                combo.getOrden(),
                incluye
        );
    }
}
