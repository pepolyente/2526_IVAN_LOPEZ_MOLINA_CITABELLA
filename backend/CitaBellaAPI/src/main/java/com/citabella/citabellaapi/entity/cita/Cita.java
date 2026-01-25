package com.citabella.citabellaapi.entity.cita;


import com.citabella.citabellaapi.entity.cliente.Cliente;
import com.citabella.citabellaapi.entity.empleado.Empleado;
import com.citabella.citabellaapi.entity.servicio.Servicio;
import com.citabella.citabellaapi.entity.utiles.EstadoCita;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCita;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado;

    @Column(columnDefinition = "TEXT")
    private String notas;

    //añadir atributo solape
    @Column(name = "solape")
    private Boolean tieneSolape;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_empleado",nullable = false)
    private Empleado empleado;

    //Posibilidad de varios servicios por cita
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cita_servicio",
            joinColumns = @JoinColumn(name = "id_cita"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio"))
    private Set<Servicio> servicio;

    @PrePersist
    private void prePersist() {
        if (estado == null) {
            estado = EstadoCita.PENDIENTE;
        }
    }
}
