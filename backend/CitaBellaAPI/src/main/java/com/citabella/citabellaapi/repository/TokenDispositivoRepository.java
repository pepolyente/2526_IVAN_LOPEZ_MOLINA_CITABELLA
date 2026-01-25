package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.cliente.TokenDispositivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDispositivoRepository extends JpaRepository<TokenDispositivo, Integer> {
}
