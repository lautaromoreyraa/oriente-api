package com.oriente.landing.repository;

import com.oriente.landing.domain.entity.HeroSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroSectionRepository extends JpaRepository<HeroSection, Long> {
    List<HeroSection> findByActiveTrue();
}
