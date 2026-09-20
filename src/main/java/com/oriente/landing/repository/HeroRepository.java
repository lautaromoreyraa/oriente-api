package com.oriente.landing.repository;

import com.oriente.landing.domain.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeroRepository extends JpaRepository<Hero, Long> {

    /** La landing muestra un solo hero: el primero activo. */
    Optional<Hero> findFirstByActivoTrueOrderByIdAsc();

    /** Para el panel, que edita el hero exista o no este activo. */
    Optional<Hero> findFirstByOrderByIdAsc();
}
