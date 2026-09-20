package com.oriente.landing.repository;

import com.oriente.landing.domain.Combo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComboRepository extends JpaRepository<Combo, Long> {

    @EntityGraph(attributePaths = "items")
    List<Combo> findAllByActivoTrueOrderByOrdenAsc();

    @EntityGraph(attributePaths = "items")
    List<Combo> findAllByOrderByOrdenAsc();

    @EntityGraph(attributePaths = "items")
    Optional<Combo> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
