package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.seguridad.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {
}
