package com.citabella.citabellaapi.entity.seguridad;

import com.citabella.citabellaapi.entity.utiles.EstadoCuenta;
import com.citabella.citabellaapi.entity.utiles.TipoPerfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(nullable = false, unique = true,length = 100)
    private String nombreUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(value = EnumType.STRING)
    private TipoPerfil tipoPerfil;

    @Enumerated(value = EnumType.STRING)
    private EstadoCuenta estadoCuenta;

    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @PrePersist
    private void prePersist() {
        if (tipoPerfil == null) {
            tipoPerfil = TipoPerfil.NONE;
        }
        if (estadoCuenta == null) {
            estadoCuenta = EstadoCuenta.ACTIVO;
        }
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

}
