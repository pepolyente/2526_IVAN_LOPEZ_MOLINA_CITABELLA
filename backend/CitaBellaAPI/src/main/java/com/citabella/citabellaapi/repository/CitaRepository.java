package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.cita.Cita;
import com.citabella.citabellaapi.entity.empleado.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface CitaRepository extends JpaRepository<Cita,Integer> {

        //añadir campos para la nueva entidad peticion (countByEmpleadoAndFechaOverlapAndEstado(empleado, inicio, fin, ESTADO_CONFIRMADA))
        @Query(""" 
SELECT COUNT(c) > 0 FROM Cita c WHERE c.empleado.idEmpleado = :idEmpleado AND c.fechaInicio < :fechaFin AND c.fechaFin < :fechaInicio""")
        boolean existeSolape(@Param("idEmpleado")Integer idEmpleado,
                             @Param("fechaInicio")LocalDateTime fechaInicio,
                             @Param("fechaFin")LocalDateTime fechaFin);

        List<Cita> findByEmpleado_IdEmpleado(Integer idEmpleado);
        List<Cita> findByCliente_IdCliente(Integer idCliente);

        //metodo (dame todas las citas de este empleado entre hora A y hora B que no esten canceladas)



}
