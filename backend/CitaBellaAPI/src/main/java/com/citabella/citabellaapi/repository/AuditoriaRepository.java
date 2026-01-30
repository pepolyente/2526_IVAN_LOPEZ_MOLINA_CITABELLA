package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.security.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditLog, Integer> {
}
