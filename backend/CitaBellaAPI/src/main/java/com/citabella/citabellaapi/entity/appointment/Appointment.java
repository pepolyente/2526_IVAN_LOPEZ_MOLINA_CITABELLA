package com.citabella.citabellaapi.entity.appointment;

import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.entity.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "has_overlap")
    private Boolean hasOverlap;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointment_treatment",
            joinColumns = @JoinColumn(name = "id_appointment"),
            inverseJoinColumns = @JoinColumn(name = "id_treatment"))
    private List<Treatment> treatments = new ArrayList<>();

    @OneToMany(mappedBy = "appointment")
    private List<AppointmentSubmission> submissions = new ArrayList<>();


    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = AppointmentStatus.PENDING;
        }
    }
}
