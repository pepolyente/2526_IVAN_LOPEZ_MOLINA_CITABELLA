package com.citabella.citabellaapi.entity.employee;

import com.citabella.citabellaapi.entity.appointment.Cita;
import com.citabella.citabellaapi.entity.security.Usuario;
import com.citabella.citabellaapi.entity.sale.Venta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    @Column(nullable = false, length = 100, unique = true)//cambiar db a unique
    private String nombre;

    @Column(length = 100)
    private String puesto;//revisar si cambiar a enum o no tanto aqui como en la base de datos

    private BigDecimal comision;

    private Boolean activo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "empleado")
    private List<Cita> citas;

    @OneToMany(mappedBy = "empleado")
    private List<Venta> ventas;

    @PrePersist
    private void prePersist() {
        if (activo == null) activo = true;
        if (comision == null) comision = BigDecimal.ZERO;
    }

}
