package com.oriente.landing.controller;

import com.oriente.landing.dto.request.HeroSectionRequest;
import com.oriente.landing.dto.response.HeroSectionResponse;
import com.oriente.landing.service.HeroSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hero-sections")
@RequiredArgsConstructor
public class HeroSectionController {

    private final HeroSectionService service;

    @GetMapping
    public ResponseEntity<List<HeroSectionResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroSectionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<HeroSectionResponse> create(@Valid @RequestBody HeroSectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeroSectionResponse> update(@PathVariable Long id, @Valid @RequestBody HeroSectionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
