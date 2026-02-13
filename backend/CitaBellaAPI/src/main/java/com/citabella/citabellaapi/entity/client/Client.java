package com.citabella.citabellaapi.entity.client;

import com.citabella.citabellaapi.entity.appointment.Appointment;
import com.citabella.citabellaapi.entity.enums.Gender;
import com.citabella.citabellaapi.entity.security.User;
import com.citabella.citabellaapi.entity.sale.Sale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true,length = 20)
    private String phoneNumber;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", unique = true)
    private User user;

    private boolean active;

    @OneToMany(mappedBy = "client")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "client")
    private List<Sale> sales;
}
