package com.msmarik.HouseReservationSystem.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Entity
public class House {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String address;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private Long priceInCents;
    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "house")
    private List<HouseDay> houseDayList;

    public BigDecimal getPrice() {
        return BigDecimal.valueOf(priceInCents).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public void setPrice(BigDecimal price) {
        this.priceInCents = price.multiply(BigDecimal.valueOf(100)).longValueExact();
    }

}
