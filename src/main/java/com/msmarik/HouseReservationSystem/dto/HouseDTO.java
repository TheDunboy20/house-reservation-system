package com.msmarik.HouseReservationSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class HouseDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private BigDecimal price;
    private byte[] image;
}
