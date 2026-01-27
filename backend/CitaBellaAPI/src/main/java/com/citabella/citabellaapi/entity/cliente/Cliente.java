package com.citabella.citabellaapi.entity.cliente;


import com.citabella.citabellaapi.entity.cita.Cita;
import com.citabella.citabellaapi.entity.utiles.Genero;
import com.citabella.citabellaapi.entity.seguridad.Usuario;
import com.citabella.citabellaapi.entity.venta.Venta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true,length = 20)
    private String telefono;

    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario",unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "cliente")
    private List<Cita> citas;

    @OneToMany(mappedBy = "cliente")
    private List<Venta> ventas;
}
