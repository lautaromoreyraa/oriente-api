package com.oriente.landing.repository;

import com.oriente.landing.domain.InformacionDeContacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InformacionDeContactoRepository extends JpaRepository<InformacionDeContacto, Long> {

    /** Hay una sola fila: la del consultorio. */
    Optional<InformacionDeContacto> findFirstByOrderByIdAsc();
}
