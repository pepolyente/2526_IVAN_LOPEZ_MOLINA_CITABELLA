package com.citabella.citabellaapi.entity.employee;

import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.enums.EmployeePosition;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.entity.sale.Sale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(length = 100)
    private String position;

    private BigDecimal commission;

    private Boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", unique = true)
    private User user;

    @OneToMany(mappedBy = "employee")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "employee")
    private List<Sale> sales;

    @PrePersist
    private void prePersist() {
        if (active == null) active = true;
        if (commission == null) commission = BigDecimal.ZERO;
    }

}
