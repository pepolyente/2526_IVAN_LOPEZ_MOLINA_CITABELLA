package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Appointment, Integer> {

        //añadir campos para la nueva entidad peticion (countByEmpleadoAndFechaOverlapAndEstado(employee, inicio, fin, ESTADO_CONFIRMADA))
        @Query(""" 
                SELECT COUNT(c) > 0 FROM Appointment c WHERE c.employee.id = :idEmpleado AND c.startAt < :fechaFin AND c.endAt < :fechaInicio""")
        boolean existeSolape(@Param("idEmpleado")Integer idEmpleado,
                             @Param("fechaInicio")LocalDateTime fechaInicio,
                             @Param("fechaFin")LocalDateTime fechaFin);

        List<Appointment> findByEmpleado_IdEmpleado(Integer idEmpleado);

        List<Appointment> findByCliente_IdCliente(Integer idCliente);

        //metodo (dame todas las citas de este employee entre hora A y hora B que no esten canceladas)



}
