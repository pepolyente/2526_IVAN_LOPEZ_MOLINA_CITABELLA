package com.citabella.citabellaapi.entity.cita;


import com.citabella.citabellaapi.entity.cliente.Cliente;
import com.citabella.citabellaapi.entity.empleado.Empleado;
import com.citabella.citabellaapi.entity.servicio.Servicio;
import com.citabella.citabellaapi.entity.utiles.EstadoCita;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado",nullable = false)
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio",nullable = false)
    private Servicio servicio;

    @PrePersist
    private void prePersist() {
        if (estado == null) {
            estado = EstadoCita.pendiente;
        }
    }
}
