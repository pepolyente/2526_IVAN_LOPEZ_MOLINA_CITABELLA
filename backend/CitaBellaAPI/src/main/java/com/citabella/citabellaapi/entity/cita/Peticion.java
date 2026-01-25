package com.citabella.citabellaapi.entity.cita;

import com.citabella.citabellaapi.entity.servicio.Servicio;
import com.citabella.citabellaapi.entity.utiles.EstadoPeticion;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "peticion")
public class Peticion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime fechaPropuesta;

    @ManyToMany
    @JoinTable(
            name = "peticion_servicio",
            joinColumns = @JoinColumn(name = "id_peticion"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio"))
    private Set<Servicio> serviciosSolicitados;

    @Enumerated(value = EnumType.STRING)
    private EstadoPeticion estado;


}
