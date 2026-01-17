package com.msmarik.HouseReservationSystem.service;

import com.msmarik.HouseReservationSystem.model.entity.House;
import com.msmarik.HouseReservationSystem.model.entity.HouseDay;
import com.msmarik.HouseReservationSystem.repository.HouseDayRepository;
import com.msmarik.HouseReservationSystem.repository.HouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HouseService {
    private final HouseRepository houseRepository;
    private final HouseDayRepository houseDayRepository;

    public HouseService(HouseRepository houseRepository,
                        HouseDayRepository houseDayRepository) {
        this.houseRepository = houseRepository;
        this.houseDayRepository = houseDayRepository;
    }

    public List<House> getHouses() {
        return houseRepository.findAll();
    }

    public Optional<House> getHouse(Long id) {
        return houseRepository.findById(id);
    }

    public Optional<House> updateHouse(Long id, House updatedHouse) {
        Optional<House> originalHouse = houseRepository.findById(id);
        if (originalHouse.isPresent()) {
            House houseToUpdate = originalHouse.get();
            houseToUpdate.setName(updatedHouse.getName());
            houseToUpdate.setAddress(updatedHouse.getAddress());
            houseToUpdate.setAvailableFrom(updatedHouse.getAvailableFrom());
            houseToUpdate.setAvailableTo(updatedHouse.getAvailableTo());

            return Optional.of(houseRepository.save(houseToUpdate));
        } else {
            return Optional.empty();
        }
    }

    public House save(House house) {
        List<HouseDay> houseDays = new ArrayList<>();
        LocalDate startDate = house.getAvailableFrom();

        while (!startDate.isAfter(house.getAvailableTo())) {
            HouseDay houseDay = new HouseDay(startDate, null, house);
            houseDays.add(houseDay);
            startDate = startDate.plusDays(1);
        }

        houseDayRepository.saveAll(houseDays);
        return houseRepository.save(house);
    }
}
