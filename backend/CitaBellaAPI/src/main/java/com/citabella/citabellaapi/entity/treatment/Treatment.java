package com.citabella.citabellaapi.entity.treatment;


import com.citabella.citabellaapi.entity.appointment.Appointment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "treatment")
@Getter
@Setter
@NoArgsConstructor
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer minimumDuration;

    private Integer maximumDuration;

    @Column(nullable = false)
    private BigDecimal price;

    private Boolean active;

    @ManyToMany(mappedBy = "treatments")
    private List<Appointment> appointments;

    @PrePersist
    private void prePersist() {
        if (active == null) active = true;
    }

}
