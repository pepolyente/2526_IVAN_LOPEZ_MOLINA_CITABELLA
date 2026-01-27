package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.security.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {
}
