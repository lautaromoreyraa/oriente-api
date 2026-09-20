package com.oriente.landing.controller.administracion;

import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramRequest;
import com.oriente.landing.dto.administracion.instagram.PublicacionDeInstagramResponse;
import com.oriente.landing.service.administracion.instagram.PublicacionDeInstagramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Las publicaciones de Instagram que se muestran en la landing, por URL. */
@RestController
@RequestMapping("/api/admin/v1/instagram")
public class InstagramController {

    private final PublicacionDeInstagramService publicacionService;

    public InstagramController(PublicacionDeInstagramService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDeInstagramResponse>> listar() {
        return ResponseEntity.ok(publicacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDeInstagramResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(publicacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<PublicacionDeInstagramResponse> crear(
            @Valid @RequestBody PublicacionDeInstagramRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicacionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDeInstagramResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PublicacionDeInstagramRequest request) {
        return ResponseEntity.ok(publicacionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        publicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
