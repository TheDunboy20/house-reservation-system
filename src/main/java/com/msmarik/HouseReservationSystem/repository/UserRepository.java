package com.msmarik.HouseReservationSystem.repository;

import com.msmarik.HouseReservationSystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
