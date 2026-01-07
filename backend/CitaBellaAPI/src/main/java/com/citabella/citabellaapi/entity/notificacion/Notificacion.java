package com.citabella.citabellaapi.entity.notificacion;

import com.citabella.citabellaapi.entity.cliente.Cliente;
import com.citabella.citabellaapi.entity.utiles.CanalNotificacion;
import com.citabella.citabellaapi.entity.utiles.EstadoNotificacion;
import com.citabella.citabellaapi.entity.utiles.TipoNotificacion;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotificacion;

    @Enumerated(value = EnumType.STRING)
    private TipoNotificacion tipo;

    @Column(length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Enumerated(value = EnumType.STRING)
    private CanalNotificacion canal;

    @Enumerated(value = EnumType.STRING)
    private EstadoNotificacion estado;

    private LocalDateTime fechaEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @PrePersist
    private void prePersist() {
        if (estado == null) {
            estado = EstadoNotificacion.pendiente;
        }
    }

}
