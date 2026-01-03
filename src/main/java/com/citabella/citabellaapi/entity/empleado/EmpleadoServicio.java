package com.citabella.citabellaapi.entity.empleado;

import com.citabella.citabellaapi.entity.servicio.Servicio;
import jakarta.persistence.*;

@Entity
@Table(name = "empleado_servicio")
public class EmpleadoServicio {

    @EmbeddedId
    private EmpleadoServicioId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idEmpleado")
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idServicio")
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

}
