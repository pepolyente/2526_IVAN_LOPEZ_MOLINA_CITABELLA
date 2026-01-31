package com.citabella.citabellaapi.entity.client;

import com.citabella.citabellaapi.entity.enums.DeviceType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_token")
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private LocalDateTime registeredAt;

    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @PrePersist
    private void prePersist() {
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
        if (active == null) {
            active = true;
        }
    }

}
