package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.producto.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
}
