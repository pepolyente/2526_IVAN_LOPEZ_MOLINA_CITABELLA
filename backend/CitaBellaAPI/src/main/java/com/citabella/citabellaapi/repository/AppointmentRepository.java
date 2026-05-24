package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

        @Query("""
                SELECT COUNT(c) > 0 FROM Appointment c
                WHERE c.employee.id = :id
                AND c.startAt < :endAt
                AND c.endAt > :startAt""")
        boolean hasOverlap(@Param("id") Integer id,
                           @Param("startAt") LocalDateTime startAt,
                           @Param("endAt") LocalDateTime endAt);

        List<Appointment> findByEmployee_Id(Integer id);

        Page<Appointment> findByClient(Client client, Pageable pageable);

        Page<Appointment> findAllByStatus(AppointmentStatus status, Pageable pageable);



}
