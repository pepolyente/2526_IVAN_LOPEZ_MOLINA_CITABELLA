package com.citabella.citabellaapi.entity.security;

import com.citabella.citabellaapi.entity.enums.AuditAction;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String affectedTable;

    private Integer affectedId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    private LocalDateTime occurredAt;

    @Column(columnDefinition = "json")
    private String jsonDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @PrePersist
    private void prePersist() {
        if (occurredAt == null) {
            occurredAt = LocalDateTime.now();
        }
    }

}
