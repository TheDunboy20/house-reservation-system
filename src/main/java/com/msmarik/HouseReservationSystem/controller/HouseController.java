package com.msmarik.HouseReservationSystem.controller;

import com.msmarik.HouseReservationSystem.model.entity.House;
import com.msmarik.HouseReservationSystem.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("houses")
public class HouseController {
    private static final Logger log = LoggerFactory.getLogger(HouseController.class);
    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public List<House> getHouses(){
        List<House> houseList = houseService.getHouses();
        log.info("Retrieved houses: {}", houseList);
        return houseList;
    }

    @GetMapping("{id}")
    public ResponseEntity<House> getHouse(@PathVariable Long id){
        return houseService.getHouse(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<House> updateHouse(@PathVariable Long id, @RequestBody House houseDetails){
        return houseService.updateHouse(id, houseDetails)
                .map(updatedHouse -> ResponseEntity.ok().body(updatedHouse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<House> deleteHouse(@PathVariable Long id){
        return houseService.deleteHouse(id)
                .map(deletedHouse -> ResponseEntity.ok().body(deletedHouse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public House save(@RequestBody House house){
        log.info("Saving house: {}", house);
        return houseService.save(house);
    }
}
