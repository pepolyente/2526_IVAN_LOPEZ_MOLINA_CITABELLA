package com.citabella.citabellaapi.entity.employee;

import com.citabella.citabellaapi.entity.treatment.Servicio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empleado_servicio")
@Getter
@Setter
@NoArgsConstructor
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
