package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

        @Query(""" 
                SELECT COUNT(c) > 0 FROM Appointment c WHERE c.employee.id = :id AND c.startAt < :endAt AND c.endAt < :startAt""")
        boolean hasOverlap(@Param("id") Integer id,
                           @Param("startAt") LocalDateTime startAt,
                           @Param("endAt") LocalDateTime fechaFin);

        List<Appointment> findByEmployee_Id(Integer id);

        List<Appointment> findByClient_Id(Integer id);

        //TODO (findAllWhere between Hour A and B not canceled from Employee)



}
