package com.oriente.landing.repository;

import com.oriente.landing.domain.Servicio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // El EntityGraph trae las imagenes del carrusel en la misma consulta: sin esto
    // la landing dispara una consulta por servicio al mapear.
    @EntityGraph(attributePaths = "imagenes")
    List<Servicio> findAllByActivoTrueOrderByOrdenAsc();

    @EntityGraph(attributePaths = "imagenes")
    List<Servicio> findAllByOrderByOrdenAsc();

    @EntityGraph(attributePaths = "imagenes")
    Optional<Servicio> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
