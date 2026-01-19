package com.msmarik.HouseReservationSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HouseDayDTO {
    private Long id;
    private LocalDate date;
    private Long reservedByUserId;
    private String reservedByUsername;
    private Long houseId;
}
