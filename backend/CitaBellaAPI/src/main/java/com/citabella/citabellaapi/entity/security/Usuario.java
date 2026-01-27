package com.citabella.citabellaapi.entity.security;

import com.citabella.citabellaapi.entity.client.Cliente;
import com.citabella.citabellaapi.entity.employee.Empleado;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.enums.ProfileType;
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
    private ProfileType profileType;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus;

    private LocalDateTime fechaRegistro;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @OneToOne(mappedBy = "usuario")
    private Cliente cliente;

    @OneToOne(mappedBy = "usuario")
    private Empleado empleado;

    @PrePersist
    private void prePersist() {
        if (profileType == null) {
            profileType = ProfileType.NONE;
        }
        if (accountStatus == null) {
            accountStatus = AccountStatus.ACTIVE;
        }
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    public void asignarCliente(Cliente cliente) {
        if (profileType != ProfileType.NONE) {
            throw new IllegalStateException("Perfil ya asignado");
        }
        this.profileType = ProfileType.CLIENT;
        this.cliente = cliente;
    }

    public void asignarEmpleado(Empleado empleado) {
        if (profileType != ProfileType.NONE) {
            throw new IllegalStateException("Perfil ya asignado");
        }
        this.profileType = ProfileType.EMPLOYEE;
        this.empleado = empleado;
    }

    public boolean esCliente() {
        return profileType == ProfileType.CLIENT;
    }

    public boolean esEmpleado() {
        return profileType == ProfileType.EMPLOYEE;
    }


}
