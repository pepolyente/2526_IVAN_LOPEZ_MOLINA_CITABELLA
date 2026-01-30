package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notification, Integer> {
}
