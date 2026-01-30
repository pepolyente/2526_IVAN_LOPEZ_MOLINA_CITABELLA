package com.citabella.citabellaapi.entity.employee;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_treatment")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeTreatment {

    @EmbeddedId
    private EmployeeTreatmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("treatmentId")
    @JoinColumn(name = "id_treatment")
    private Treatment treatment;
}
