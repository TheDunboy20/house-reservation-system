package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<HouseDay> reservations;
}
