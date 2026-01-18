package com.msmarik.HouseReservationSystem.controller;

import com.msmarik.HouseReservationSystem.dto.HouseDayDTO;
import com.msmarik.HouseReservationSystem.service.HouseDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    public HouseDayService houseDayService;

    public ReservationController(HouseDayService houseDayService) {
        this.houseDayService = houseDayService;
    }

    @GetMapping
    public List<HouseDayDTO> getReservations(){
        return houseDayService.getAllHouseDays();
    }

    @GetMapping("{id}")
    public ResponseEntity<HouseDayDTO> getReservation(@PathVariable Long id){
        return houseDayService.getHouseDay(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<HouseDayDTO> updateReservation(@RequestBody HouseDayDTO houseDayDTO, @PathVariable Long id){
        return houseDayService.updateHouseDay(id, houseDayDTO)
                .map(updatedHouseDay -> ResponseEntity.ok().body(updatedHouseDay))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
