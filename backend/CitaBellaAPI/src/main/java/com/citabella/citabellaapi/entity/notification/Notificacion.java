package com.citabella.citabellaapi.entity.notification;

import com.citabella.citabellaapi.entity.client.Cliente;
import com.citabella.citabellaapi.entity.enums.NotificationChannel;
import com.citabella.citabellaapi.entity.enums.NotificationStatus;
import com.citabella.citabellaapi.entity.enums.NotificationType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotificacion;

    @Enumerated(value = EnumType.STRING)
    private NotificationType tipo;

    @Column(length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Enumerated(value = EnumType.STRING)
    private NotificationChannel canal;

    @Enumerated(value = EnumType.STRING)
    private NotificationStatus estado;

    private LocalDateTime fechaEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @PrePersist
    private void prePersist() {
        if (estado == null) {
            estado = NotificationStatus.PENDING;
        }
    }

}
