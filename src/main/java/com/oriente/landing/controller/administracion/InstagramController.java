package com.oriente.landing.controller.administracion;

import com.oriente.landing.dto.administracion.instagram.ImportacionDeInstagramRequest;
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
import org.springframework.web.bind.annotation.RequestParam;

/** Los reels y posteos de Instagram que ilustran cada servicio. */
@RestController
@RequestMapping("/api/admin/v1/instagram")
public class InstagramController {

    private final PublicacionDeInstagramService publicacionService;

    public InstagramController(PublicacionDeInstagramService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDeInstagramResponse>> listar(@RequestParam Long servicioId) {
        return ResponseEntity.ok(publicacionService.listarDelServicio(servicioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDeInstagramResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(publicacionService.obtenerPorId(id));
    }

    /** Agrega una publicacion solo con el link: el contenido se trae de Instagram. */
    @PostMapping("/importar")
    public ResponseEntity<PublicacionDeInstagramResponse> importar(
            @Valid @RequestBody ImportacionDeInstagramRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicacionService.importar(request.servicioId(), request.url()));
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
