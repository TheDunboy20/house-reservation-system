package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class House {
    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private String address;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private Long priceInCents;
    private boolean deleted;
    private byte[] image;

    // Store created by user

    @OneToMany(mappedBy = "house")
    private List<HouseDay> houseDayList;

    public BigDecimal getPrice() {
        return BigDecimal.valueOf(priceInCents).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public void setPrice(BigDecimal price) {
        this.priceInCents = price.multiply(BigDecimal.valueOf(100)).longValueExact();
    }

}
