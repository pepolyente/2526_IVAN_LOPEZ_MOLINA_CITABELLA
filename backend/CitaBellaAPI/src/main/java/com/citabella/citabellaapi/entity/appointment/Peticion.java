package com.citabella.citabellaapi.entity.appointment;

import com.citabella.citabellaapi.entity.client.Cliente;
import com.citabella.citabellaapi.entity.employee.Empleado;
import com.citabella.citabellaapi.entity.treatment.Servicio;
import com.citabella.citabellaapi.entity.enums.CanalPeticion;
import com.citabella.citabellaapi.entity.enums.EstadoPeticion;
import com.citabella.citabellaapi.entity.enums.TipoPeticion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "peticion")
@Getter
@Setter
@NoArgsConstructor
public class Peticion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime fechaPropuesta;

    @Enumerated(value = EnumType.STRING)
    private EstadoPeticion estado;

    @Enumerated(EnumType.STRING)
    private CanalPeticion canal;

    @Enumerated(EnumType.STRING)
    private TipoPeticion tipo;

    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "peticion_servicio",
            joinColumns = @JoinColumn(name = "id_peticion"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio"))
    private Set<Servicio> serviciosSolicitados;

    @OneToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_cita")
    private Cita cita;


    @PrePersist
    void prePersist() {
        cita = null;
        estado = EstadoPeticion.PENDIENTE;
    }

    private LocalDateTime calcularFechaFin(LocalDateTime fechaInicio, Set<Servicio> servicios) {
        Integer tiempoTotal = 0;
        for (Servicio servicio : servicios) {
            tiempoTotal += servicio.getDuracionMin();
        }
        return fechaInicio.plusMinutes(tiempoTotal);
    }


}
