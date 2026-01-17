package com.msmarik.HouseReservationSystem.repository;

import com.msmarik.HouseReservationSystem.model.entity.HouseDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseDayRepository extends JpaRepository<HouseDay,Long> {}
