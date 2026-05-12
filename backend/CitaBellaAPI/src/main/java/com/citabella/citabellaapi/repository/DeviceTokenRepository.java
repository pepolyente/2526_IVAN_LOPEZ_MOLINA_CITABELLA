package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.client.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Integer> {
}
