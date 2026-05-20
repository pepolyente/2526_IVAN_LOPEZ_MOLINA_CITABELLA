package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByUser_Id(Integer id);

    List<Client> findAllByActive(Boolean active);

    Page<Client> findAllByActive(Boolean active, Pageable pageable);
}
