package com.msmarik.HouseReservationSystem.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}
