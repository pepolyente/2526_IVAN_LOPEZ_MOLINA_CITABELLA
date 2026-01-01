package com.citabella.citabellaapi.entity.utiles;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmpleadoServicioId implements Serializable {

    @Column(name = "id_empleado",nullable = false)
    private Integer idEmpleado;

    @Column(name = "id_servicio",nullable = false)
    private Integer idServicio;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmpleadoServicioId that)) return false;
        return Objects.equals(idEmpleado, that.idEmpleado) && Objects.equals(idServicio, that.idServicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpleado, idServicio);
    }
}
