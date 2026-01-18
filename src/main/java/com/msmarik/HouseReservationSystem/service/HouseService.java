package com.msmarik.HouseReservationSystem.service;

import com.msmarik.HouseReservationSystem.dto.HouseDTO;
import com.msmarik.HouseReservationSystem.dto.HouseDayDTO;
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
import java.util.stream.Collectors;

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

    public List<HouseDTO> getHouses() {
        final List<House> foundHouses = houseRepository.findAllByDeleted(false);
        return foundHouses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<HouseDTO> getHouse(Long id) {
        final Optional<House> foundHouse = houseRepository.findByIdAndDeleted(id, false);
        if (foundHouse.isPresent()) {
            final HouseDTO houseDTO = convertToDTO(foundHouse.get());
            return Optional.of(houseDTO);
        }
        return Optional.empty();
    }

    public Optional<HouseDTO> updateHouse(Long id, House updatedHouse) {
         final Optional<House> originalHouse = houseRepository.findByIdAndDeleted(id, false);
        if (originalHouse.isPresent()) {
            final House houseToUpdate = originalHouse.get();
            houseToUpdate.setName(updatedHouse.getName());
            houseToUpdate.setAddress(updatedHouse.getAddress());
            houseToUpdate.setAvailableFrom(updatedHouse.getAvailableFrom());
            houseToUpdate.setAvailableTo(updatedHouse.getAvailableTo());

            House savedHouse = houseRepository.save(houseToUpdate);
            return Optional.of(convertToDTO(savedHouse));
        } else {
            return Optional.empty();
        }
    }

    public Optional<HouseDTO> deleteHouse(Long id) {
        final Optional<House> houseToDelete = houseRepository.findByIdAndDeleted(id, false);
        if (houseToDelete.isPresent()) {
            final House house = houseToDelete.get();
            house.setDeleted(true);
            final List<HouseDay> houseDays = houseDayRepository.findAllByHouseIdAndDeleted(id, false);
            houseDays.forEach(houseDay -> houseDay.setDeleted(true));
            houseDayRepository.saveAll(houseDays);

            final House savedHouse = houseRepository.save(house);
            return Optional.of(convertToDTO(savedHouse));
        } else {
            return Optional.empty();
        }
    }

    public HouseDTO save(House house) {
        final List<HouseDay> houseDays = new ArrayList<>();
        LocalDate startDate = house.getAvailableFrom();

        final House savedHouse = houseRepository.save(house);

        while (!startDate.isAfter(house.getAvailableTo())) {
            final HouseDay houseDay = new HouseDay(startDate, null, savedHouse, false);
            houseDays.add(houseDay);
            startDate = startDate.plusDays(1);
        }

        houseDayRepository.saveAll(houseDays);

        return convertToDTO(savedHouse);
    }

    public List<HouseDayDTO> getHouseDays(Long id) {
        final List<HouseDay> houseDays = houseDayRepository.findAllByHouseIdAndDeleted(id, false);
        return houseDays.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private HouseDTO convertToDTO(House house) {
        final HouseDTO houseDTO = new HouseDTO();
        houseDTO.setId(house.getId());
        houseDTO.setName(house.getName());
        houseDTO.setDescription(house.getDescription());
        houseDTO.setAddress(house.getAddress());
        houseDTO.setAvailableFrom(house.getAvailableFrom());
        houseDTO.setAvailableTo(house.getAvailableTo());
        houseDTO.setPrice(house.getPrice());
        return houseDTO;
    }

    private HouseDayDTO convertToDTO(HouseDay houseDay) {
        final HouseDayDTO houseDayDTO = new HouseDayDTO();
        houseDayDTO.setId(houseDay.getId());
        houseDayDTO.setDate(houseDay.getDate());
        houseDayDTO.setHouseId(houseDay.getHouse().getId());
        if (houseDay.getReservedBy() != null) {
            houseDayDTO.setReservedByUserId(houseDay.getReservedBy().getId());
        }
        return houseDayDTO;
    }
}
