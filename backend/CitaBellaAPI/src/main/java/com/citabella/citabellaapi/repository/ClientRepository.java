package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    boolean existsByPhoneNumber(String username);

    boolean existsByUser_Id(Integer id);

    List<Client> findAllByActive(boolean active);
}
