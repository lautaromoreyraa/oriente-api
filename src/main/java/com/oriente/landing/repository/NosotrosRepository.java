package com.oriente.landing.repository;

import com.oriente.landing.domain.Nosotros;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NosotrosRepository extends JpaRepository<Nosotros, Long> {

    // Sin EntityGraph a proposito: Nosotros tiene tres listas y pedirlas juntas en
    // una sola consulta termina en MultipleBagFetchException. Los services que la
    // usan son transaccionales y las resuelven al mapear.
    Optional<Nosotros> findFirstByActivoTrueOrderByIdAsc();

    Optional<Nosotros> findFirstByOrderByIdAsc();
}
