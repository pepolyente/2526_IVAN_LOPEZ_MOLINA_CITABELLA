package com.citabella.citabellaapi.entity.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class EmployeeTreatmentId implements Serializable {

    @Column(name = "id_employee", nullable = false)
    private Integer employeeId;

    @Column(name = "id_treatment", nullable = false)
    private Integer treatmentId;

    public EmployeeTreatmentId(Integer treatmentId, Integer employeeId) {
        this.treatmentId = treatmentId;
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmployeeTreatmentId that)) return false;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(treatmentId, that.treatmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, treatmentId);
    }
}
