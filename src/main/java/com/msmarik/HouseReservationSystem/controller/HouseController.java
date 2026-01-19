package com.msmarik.HouseReservationSystem.controller;

import com.msmarik.HouseReservationSystem.dto.HouseDTO;
import com.msmarik.HouseReservationSystem.dto.HouseDayDTO;
import com.msmarik.HouseReservationSystem.model.entity.House;
import com.msmarik.HouseReservationSystem.service.HouseDayService;
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
    private final HouseDayService houseDayService;

    public HouseController(HouseService houseService, HouseDayService houseDayService) {
        this.houseService = houseService;
        this.houseDayService = houseDayService;
    }

    @GetMapping
    public List<HouseDTO> getHouses(){
        List<HouseDTO> houseList = houseService.getHouses();
        log.info("Retrieved houses: {}", houseList);
        return houseList;
    }

    @GetMapping("{id}")
    public ResponseEntity<HouseDTO> getHouse(@PathVariable Long id){
        return houseService.getHouse(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<HouseDTO> updateHouse(@PathVariable Long id, @RequestBody House houseDetails){
        return houseService.updateHouse(id, houseDetails)
                .map(updatedHouse -> ResponseEntity.ok().body(updatedHouse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HouseDTO> deleteHouse(@PathVariable Long id){
        return houseService.deleteHouse(id)
                .map(deletedHouse -> ResponseEntity.ok().body(deletedHouse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public HouseDTO save(@RequestBody House house){
        log.info("Saving house: {}", house);
        return houseService.save(house);
    }

    @GetMapping("{id}/house-days")
    public List<HouseDayDTO> getHouseDays(@PathVariable Long id){
        return houseService.getHouseDays(id);
    }

    @DeleteMapping("{id}/house-days/{houseDayId}")
    public ResponseEntity<HouseDayDTO> deleteHouseDay(@PathVariable Long id){
        return houseDayService.deleteHouseDay(id)
                .map(deletedHouseDay -> ResponseEntity.ok().body(deletedHouseDay))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
