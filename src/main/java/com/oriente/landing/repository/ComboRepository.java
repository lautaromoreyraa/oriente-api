package com.oriente.landing.repository;

import com.oriente.landing.domain.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {
    Optional<Combo> findBySlug(String slug);

    List<Combo> findByActiveTrue();
}
