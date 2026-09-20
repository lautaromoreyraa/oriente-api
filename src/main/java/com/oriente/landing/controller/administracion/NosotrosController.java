package com.oriente.landing.controller.administracion;

import com.oriente.landing.dto.administracion.nosotros.NosotrosRequest;
import com.oriente.landing.dto.administracion.nosotros.NosotrosResponse;
import com.oriente.landing.service.administracion.nosotros.NosotrosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * La seccion nosotros se guarda completa, con sus estadisticas, diferenciales y
 * equipo adentro: es un solo formulario en el panel y un solo guardado aca.
 */
@RestController
@RequestMapping("/api/admin/v1/nosotros")
public class NosotrosController {

    private final NosotrosService nosotrosService;

    public NosotrosController(NosotrosService nosotrosService) {
        this.nosotrosService = nosotrosService;
    }

    @GetMapping
    public ResponseEntity<NosotrosResponse> obtener() {
        return ResponseEntity.ok(nosotrosService.obtener());
    }

    @PutMapping
    public ResponseEntity<NosotrosResponse> guardar(@Valid @RequestBody NosotrosRequest request) {
        return ResponseEntity.ok(nosotrosService.guardar(request));
    }
}
