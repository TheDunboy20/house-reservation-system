package com.msmarik.HouseReservationSystem.service;

import com.msmarik.HouseReservationSystem.dto.HouseDayDTO;
import com.msmarik.HouseReservationSystem.model.entity.HouseDay;
import com.msmarik.HouseReservationSystem.model.entity.User;
import com.msmarik.HouseReservationSystem.repository.HouseDayRepository;
import com.msmarik.HouseReservationSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HouseDayService {
    private final HouseDayRepository houseDayRepository;
    private final UserRepository userRepository;

    public HouseDayService(HouseDayRepository houseDayRepository, UserRepository userRepository) {
        this.houseDayRepository = houseDayRepository;
        this.userRepository = userRepository;
    }

    public Optional<HouseDayDTO> updateHouseDay(Long id, HouseDayDTO updatedHouseDayDTO) {
        Optional<HouseDay> originalHouseDay = houseDayRepository.findByIdAndDeleted(id, false);
        if (originalHouseDay.isPresent()) {
            HouseDay houseDayToUpdate = originalHouseDay.get();
            Long reservedByUserId = updatedHouseDayDTO.getReservedByUserId();
            if (reservedByUserId != null) {
                User reservedByUser = userRepository.findById(reservedByUserId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + reservedByUserId));
                houseDayToUpdate.setReservedBy(reservedByUser);
            } else {
                houseDayToUpdate.setReservedBy(null); // Clear the reservation if null
            }

            HouseDay savedHouseDay = houseDayRepository.save(houseDayToUpdate);
            return Optional.of(convertToDTO(savedHouseDay));
        } else {
            return Optional.empty();
        }
    }

    public Optional<HouseDayDTO> createReservation(Long houseDayId, HouseDayDTO houseDayDTO) {
        Optional<HouseDay> originalHouseDay = houseDayRepository.findByIdAndDeleted(houseDayId, false);
        if (originalHouseDay.isPresent()) {
            HouseDay houseDayToUpdate = originalHouseDay.get();
            Long reservedByUserId = houseDayDTO.getReservedByUserId();

            if (houseDayToUpdate.getReservedBy() != null) {
                throw new IllegalStateException("This date is already reserved.");
            }

            if (reservedByUserId != null) {
                User reservedByUser = userRepository.findById(reservedByUserId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + reservedByUserId));
                houseDayToUpdate.setReservedBy(reservedByUser);
            } else {
                houseDayToUpdate.setReservedBy(null); // Clear the reservation if null
            }

            HouseDay savedHouseDay = houseDayRepository.save(houseDayToUpdate);
            return Optional.of(convertToDTO(savedHouseDay));
        } else {
            return Optional.empty();
        }
    }

    public Optional<HouseDayDTO> deleteReservation(Long houseDayId) {
        Optional<HouseDay> originalHouseDay = houseDayRepository.findByIdAndDeleted(houseDayId, false);
        if (originalHouseDay.isPresent()) {
            HouseDay houseDayToUpdate = originalHouseDay.get();

            if (houseDayToUpdate.getReservedBy() == null) {
                throw new IllegalStateException("This date is not reserved.");
            }

            houseDayToUpdate.setReservedBy(null);

            HouseDay savedHouseDay = houseDayRepository.save(houseDayToUpdate);
            return Optional.of(convertToDTO(savedHouseDay));
        } else {
            return Optional.empty();
        }
    }

    public Optional<HouseDayDTO> deleteHouseDay(Long id) {
        final Optional<HouseDay> originalHouseDay = houseDayRepository.findByIdAndDeleted(id, false);
        if (originalHouseDay.isPresent()) {
            final HouseDay houseDayToDelete = originalHouseDay.get();
            houseDayToDelete.setDeleted(true);
            HouseDay deletedHouseDay = houseDayRepository.save(houseDayToDelete);
            return Optional.of(convertToDTO(deletedHouseDay));
        } else {
            return Optional.empty();
        }
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
