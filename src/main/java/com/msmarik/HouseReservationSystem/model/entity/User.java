package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<HouseDay> reservations;
}
