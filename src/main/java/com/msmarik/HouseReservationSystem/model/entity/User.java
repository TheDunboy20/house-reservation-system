package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String email;
    @Column(nullable = false)
    private String passwordHash;

    private boolean enabled = true;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<HouseDay> reservations;
}
