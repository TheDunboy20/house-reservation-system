package com.msmarik.HouseReservationSystem.controller;

import com.msmarik.HouseReservationSystem.model.entity.House;
import com.msmarik.HouseReservationSystem.service.HouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping
    public House save(@RequestBody House house){
        log.info("Saving house: {}", house);
        return houseService.save(house);
    }
}
