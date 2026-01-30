package com.citabella.citabellaapi.entity.sale;

import com.citabella.citabellaapi.entity.treatment.Treatment;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_treatment_detail")
public class SaleTreatmentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    private BigDecimal pricePerUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sale")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_treatment")
    private Treatment treatment;

    @PrePersist
    private void prePersist() {
        if (quantity == null) {
            quantity = 1;
        }
    }

}
