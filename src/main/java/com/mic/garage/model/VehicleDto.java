package com.mic.garage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
public class VehicleDto {
    private Long id;
    private String brand;
    private int vehicleYear;
    private int engineCapacity;

    // abstract boolean exit();
}
