package com.citabella.citabellaapi.entity.sale;

import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.client.Client;
import com.citabella.citabellaapi.entity.employee.Employee;
import com.citabella.citabellaapi.entity.enums.PaymentMethod;
import com.citabella.citabellaapi.entity.enums.SaleState;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime soldAt;

    private BigDecimal totalAmount;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    private SaleState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @OneToOne(optional = true)
    @JoinColumn(name = "id_appointment")
    private Appointment appointment;

    @PrePersist
    private void prePersist() {
        if (soldAt == null) {
            soldAt = LocalDateTime.now();
        }
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.CASH;
        }
    }

}
