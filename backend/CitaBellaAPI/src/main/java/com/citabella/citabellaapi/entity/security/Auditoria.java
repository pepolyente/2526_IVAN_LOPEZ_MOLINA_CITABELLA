package com.citabella.citabellaapi.entity.security;

import com.citabella.citabellaapi.entity.enums.AccionAuditoria;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAuditoria;

    @Column(length = 100)
    private String tablaAfectada;

    private Integer idAfectado;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AccionAuditoria accion;

    private LocalDateTime fecha;

    @Column(columnDefinition = "json")
    private String detalleJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @PrePersist
    private void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

}
