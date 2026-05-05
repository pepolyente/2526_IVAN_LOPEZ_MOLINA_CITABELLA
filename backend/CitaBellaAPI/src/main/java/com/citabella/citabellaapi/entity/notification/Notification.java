package com.citabella.citabellaapi.entity.notification;

import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.enums.NotificationChannel;
import com.citabella.citabellaapi.entity.enums.NotificationStatus;
import com.citabella.citabellaapi.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@NoArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(value = EnumType.STRING)
    private NotificationChannel channel;

    @Enumerated(value = EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private Client client;

    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = NotificationStatus.PENDING;
        }
    }

}
