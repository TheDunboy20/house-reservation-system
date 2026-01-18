package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class HouseDay {
    public HouseDay (LocalDate date, User reservedBy, House house, boolean deleted) {
        this.date = date;
        this.reservedBy = reservedBy;
        this.house = house;
        this.deleted = deleted;
    }

    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private Long id;
    private LocalDate date;
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reservedBy;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;
}
