package com.citabella.citabellaapi.entity.security;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    private String token;

    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    private Boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @PrePersist
    private void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        if (revoked == null) {
            revoked = false;
        }
    }

}
