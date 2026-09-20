package com.oriente.landing.controller.administracion;

import com.oriente.landing.dto.administracion.combo.ComboRequest;
import com.oriente.landing.dto.administracion.combo.ComboResponse;
import com.oriente.landing.service.administracion.combo.ComboService;
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

@RestController
@RequestMapping("/api/admin/v1/combos")
public class ComboController {

    private final ComboService comboService;

    public ComboController(ComboService comboService) {
        this.comboService = comboService;
    }

    @GetMapping
    public ResponseEntity<List<ComboResponse>> listar() {
        return ResponseEntity.ok(comboService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComboResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(comboService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ComboResponse> crear(@Valid @RequestBody ComboRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comboService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComboResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody ComboRequest request) {
        return ResponseEntity.ok(comboService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comboService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
