package com.msmarik.HouseReservationSystem.service;

import com.msmarik.HouseReservationSystem.model.entity.House;
import com.msmarik.HouseReservationSystem.repository.HouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HouseService {
    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<House> getHouses() {
        return houseRepository.findAll();
    }

    public House save(House house) {
        final House savedHouse = houseRepository.save(house);
        // TODO: Continue here - generate HouseDay entities for the available dates
        return savedHouse;
    }
}
