package com.citabella.citabellaapi.entity.servicio;


import com.citabella.citabellaapi.entity.cita.Cita;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "servicio")
@Getter
@Setter
@NoArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicio;

    @Column(nullable = false,length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    //minutos
    @Column(nullable = false)
    private Integer duracionMin;

    private Integer duracionMax;

    @Column(nullable = false)
    private BigDecimal precio;

    private Boolean activo;

    @ManyToMany(mappedBy = "servicios")
    private List<Cita> citas;

    @PrePersist
    private void prePersist() {
        if (activo == null) activo = true;
    }

}
