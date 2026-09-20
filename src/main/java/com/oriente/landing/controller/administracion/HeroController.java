package com.oriente.landing.controller.administracion;

import com.oriente.landing.dto.administracion.hero.HeroRequest;
import com.oriente.landing.dto.administracion.hero.HeroResponse;
import com.oriente.landing.service.administracion.hero.HeroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** El hero es una seccion unica: se lee y se guarda, no se crea ni se borra. */
@RestController
@RequestMapping("/api/admin/v1/hero")
public class HeroController {

    private final HeroService heroService;

    public HeroController(HeroService heroService) {
        this.heroService = heroService;
    }

    @GetMapping
    public ResponseEntity<HeroResponse> obtener() {
        return ResponseEntity.ok(heroService.obtener());
    }

    @PutMapping
    public ResponseEntity<HeroResponse> guardar(@Valid @RequestBody HeroRequest request) {
        return ResponseEntity.ok(heroService.guardar(request));
    }
}
