package com.oriente.landing.repository;

import com.oriente.landing.domain.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findBySlug(String slug);

    List<Service> findByActiveTrue();
}
