package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String email;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<HouseDay> reservations;
}
