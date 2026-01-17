package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class HouseDay {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reservedBy;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;
}
