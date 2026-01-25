package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.seguridad.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
}
