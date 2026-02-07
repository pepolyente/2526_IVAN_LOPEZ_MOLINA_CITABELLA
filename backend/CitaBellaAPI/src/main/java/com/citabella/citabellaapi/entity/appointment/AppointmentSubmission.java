package com.citabella.citabellaapi.entity.appointment;

import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.treatment.Treatment;
import com.citabella.citabellaapi.entity.enums.RequestChannel;
import com.citabella.citabellaapi.entity.enums.RequestStatus;
import com.citabella.citabellaapi.entity.enums.RequestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(
        name = "appointment_submission",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uc_submission_client_employee_appointment",
                        columnNames = {
                                "id_client",
                                "id_employee",
                                "id_appointment"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AppointmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime proposedAt;

    @Enumerated(value = EnumType.STRING)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    private RequestChannel channel;

    @Enumerated(EnumType.STRING)
    private RequestType type;

    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "appointment_submission_treatment",
            joinColumns = @JoinColumn(name = "id_appointment_submission"),
            inverseJoinColumns = @JoinColumn(name = "id_treatment"))
    private Set<Treatment> requestedTreatments;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "id_appointment")
    private Appointment appointment;


    @PrePersist
    void prePersist() {
        appointment = null;
        status = RequestStatus.PENDING;
    }

    private LocalDateTime calculateEndAt(LocalDateTime startAt, Set<Treatment> treatments) {
        Integer totalTime = 0;
        for (Treatment treatment : treatments) {
            totalTime += treatment.getMinimumDuration();
        }
        return startAt.plusMinutes(totalTime);
    }


}
