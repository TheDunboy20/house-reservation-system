package com.msmarik.HouseReservationSystem.repository;

import com.msmarik.HouseReservationSystem.model.entity.HouseDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// DTOs until persistence
@Repository
public interface HouseDayRepository extends JpaRepository<HouseDay,Long> {
    List<HouseDay> findAllByDeleted(boolean deleted);
    List<HouseDay> findAllByHouseIdAndDeleted(Long houseId, boolean deleted);

    Optional<HouseDay> findByIdAndDeleted(Long id, boolean deleted);
}
