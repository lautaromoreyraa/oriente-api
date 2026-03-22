package com.oriente.landing.controller;

import com.oriente.landing.dto.request.AboutSectionRequest;
import com.oriente.landing.dto.response.AboutSectionResponse;
import com.oriente.landing.service.AboutSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/about-sections")
@RequiredArgsConstructor
public class AboutSectionController {

    private final AboutSectionService service;

    @GetMapping
    public ResponseEntity<List<AboutSectionResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AboutSectionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AboutSectionResponse> create(@Valid @RequestBody AboutSectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AboutSectionResponse> update(@PathVariable Long id, @Valid @RequestBody AboutSectionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
