package com.citabella.citabellaapi.entity.sale;

import com.citabella.citabellaapi.entity.appointment.Cita;
import com.citabella.citabellaapi.entity.client.Cliente;
import com.citabella.citabellaapi.entity.employee.Empleado;
import com.citabella.citabellaapi.entity.enums.PaymentMethod;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVenta;

    private LocalDateTime fecha;

    private BigDecimal total;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @OneToOne(optional = true)
    @JoinColumn(name = "id_cita")
    private Cita cita;

    @PrePersist
    private void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.CASH;
        }
    }

}
