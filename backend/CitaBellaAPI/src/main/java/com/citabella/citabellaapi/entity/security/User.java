package com.citabella.citabellaapi.entity.security;

import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.enums.AccountStatus;
import com.citabella.citabellaapi.entity.enums.ProfileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true,length = 100)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(value = EnumType.STRING)
    private ProfileType profileType;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role", unique = false)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Client client;

    @OneToOne(mappedBy = "user")
    private Employee employee;

    @PrePersist
    private void prePersist() {
        if (profileType == null) {
            profileType = ProfileType.NONE;
        }
        if (accountStatus == null) {
            accountStatus = AccountStatus.ACTIVE;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void assignClient(Client client) {
        if (profileType != ProfileType.NONE) {
            throw new IllegalStateException("Profile already assigned");
        }
        this.profileType = ProfileType.CLIENT;
        this.client = client;
    }

    public void assignEmployee(Employee employee) {
        if (profileType != ProfileType.NONE) {
            throw new IllegalStateException("Profile already assigned");
        }
        this.profileType = ProfileType.EMPLOYEE;
        this.employee = employee;
    }

    public void unassignProfile() {
        this.profileType = ProfileType.NONE;
        this.client = null;
        this.employee = null;
    }

    public boolean hasProfile() {
        return profileType != ProfileType.NONE;
    }

    public boolean isClient() {
        return profileType == ProfileType.CLIENT;
    }

    public boolean isEmployee() {
        return profileType == ProfileType.EMPLOYEE;
    }


}
