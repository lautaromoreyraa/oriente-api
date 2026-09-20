package com.oriente.landing.repository;

import com.oriente.landing.domain.PublicacionDeInstagram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionDeInstagramRepository extends JpaRepository<PublicacionDeInstagram, Long> {

    List<PublicacionDeInstagram> findAllByActivoTrueOrderByOrdenAsc();

    List<PublicacionDeInstagram> findAllByOrderByOrdenAsc();

    boolean existsByUrl(String url);

    boolean existsByUrlAndIdNot(String url, Long id);
}
