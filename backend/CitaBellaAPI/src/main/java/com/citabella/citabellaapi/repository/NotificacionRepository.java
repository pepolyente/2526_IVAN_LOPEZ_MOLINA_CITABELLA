package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.notification.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
