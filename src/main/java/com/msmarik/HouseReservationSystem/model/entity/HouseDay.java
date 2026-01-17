package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@NoArgsConstructor
public class HouseDay {
    public HouseDay (LocalDate date, User reservedBy, House house) {
        this.date = date;
        this.reservedBy = reservedBy;
        this.house = house;
    }

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reservedBy;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;
}
