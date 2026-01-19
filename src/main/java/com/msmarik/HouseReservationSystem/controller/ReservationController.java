package com.msmarik.HouseReservationSystem.controller;

import com.msmarik.HouseReservationSystem.dto.HouseDayDTO;
import com.msmarik.HouseReservationSystem.service.HouseDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    public HouseDayService houseDayService;

    public ReservationController(HouseDayService houseDayService) {
        this.houseDayService = houseDayService;
    }

    @PostMapping("{id}")
    public ResponseEntity<HouseDayDTO> createReservation(@RequestBody HouseDayDTO houseDayDTO, @PathVariable Long id){
        return houseDayService.createReservation(id, houseDayDTO)
                .map(updatedHouseDay -> ResponseEntity.ok().body(updatedHouseDay))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HouseDayDTO> deleteReservation(@PathVariable Long id){
        return houseDayService.deleteReservation(id)
                .map(updatedHouseDay -> ResponseEntity.ok().body(updatedHouseDay))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
