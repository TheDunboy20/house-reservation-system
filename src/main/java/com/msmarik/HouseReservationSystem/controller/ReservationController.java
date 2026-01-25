package com.msmarik.HouseReservationSystem.controller;

import com.msmarik.HouseReservationSystem.dto.HouseDayDTO;
import com.msmarik.HouseReservationSystem.security.AppUserDetails;
import com.msmarik.HouseReservationSystem.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    public ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("{id}")
    public ResponseEntity<HouseDayDTO> createReservation(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails) {
        return reservationService.createReservation(id, userDetails.getId())
                .map(updatedHouseDay -> ResponseEntity.ok().body(updatedHouseDay))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HouseDayDTO> deleteReservation(@PathVariable Long id){
        return reservationService.deleteReservation(id)
                .map(updatedHouseDay -> ResponseEntity.ok().body(updatedHouseDay))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
