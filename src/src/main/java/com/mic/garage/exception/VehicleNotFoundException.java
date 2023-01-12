package com.mic.garage.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long id) {
        super("Could not find vehicle "+id);
    }
}
