package com.oriente.landing.controller.administracion;

import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoRequest;
import com.oriente.landing.dto.administracion.contacto.InformacionDeContactoResponse;
import com.oriente.landing.service.administracion.contacto.InformacionDeContactoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/contacto")
public class ContactoController {

    private final InformacionDeContactoService contactoService;

    public ContactoController(InformacionDeContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @GetMapping
    public ResponseEntity<InformacionDeContactoResponse> obtener() {
        return ResponseEntity.ok(contactoService.obtener());
    }

    @PutMapping
    public ResponseEntity<InformacionDeContactoResponse> guardar(
            @Valid @RequestBody InformacionDeContactoRequest request) {
        return ResponseEntity.ok(contactoService.guardar(request));
    }
}
