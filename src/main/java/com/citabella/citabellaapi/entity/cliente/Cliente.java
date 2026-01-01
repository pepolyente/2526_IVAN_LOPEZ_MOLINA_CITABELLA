package com.citabella.citabellaapi.entity.cliente;


import com.citabella.citabellaapi.entity.utiles.Genero;
import com.citabella.citabellaapi.entity.seguridad.Usuario;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cliente")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
