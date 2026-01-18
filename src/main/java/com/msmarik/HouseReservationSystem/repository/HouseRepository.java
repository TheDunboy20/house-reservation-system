package com.msmarik.HouseReservationSystem.repository;

import com.msmarik.HouseReservationSystem.model.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House,Long> {
    List<House> findAllByDeleted(boolean deleted);

    Optional<House> findByIdAndDeleted(Long id, boolean deleted);
}
