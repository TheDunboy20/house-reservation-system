package com.msmarik.HouseReservationSystem.repository;

import com.msmarik.HouseReservationSystem.model.entity.HouseDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseDayRepository extends JpaRepository<HouseDay,Long> {
    List<HouseDay> findAllByHouseId(Long id);

    List<HouseDay> findAllByHouseIdAndDeleted(Long houseId, boolean deleted);
}
