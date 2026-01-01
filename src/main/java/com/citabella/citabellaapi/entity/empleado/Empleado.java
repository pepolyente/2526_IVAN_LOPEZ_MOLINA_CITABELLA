package com.citabella.citabellaapi.entity.empleado;

import com.citabella.citabellaapi.entity.seguridad.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String puesto;

    private BigDecimal comision;

    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @PrePersist
    private void prePersist() {
        if (activo == null) {
            activo = true;
        }
        if (comision == null) {
            comision = BigDecimal.ZERO;
        }
    }

}
