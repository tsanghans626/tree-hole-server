package com.lwznkj.treeholeserver.module.agents.repository;

import com.lwznkj.treeholeserver.module.agents.entity.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByCodeAndIsActiveTrue(String code);

    Optional<Persona> findFirstByIsActiveTrueOrderByCreatedAtDesc();

    Optional<Persona> findByCode(String code);

    Page<Persona> findByIsActiveAndCategory(Boolean isActive, String category, Pageable pageable);

    Page<Persona> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Persona> findByCategory(String category, Pageable pageable);

    Page<Persona> findAll(Pageable pageable);
}
