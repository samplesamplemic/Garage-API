package com.mic.garage.model;

import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Fuel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
//builder pattern || creational pattern ||
//When complexity of creating object increase, it can separate the instantiation process
//by using another object(a builder) to construct the object
@SuperBuilder //to use inheritance with lombok
public class CarDto extends VehicleDto {
    private Doors doors;
    private Fuel fuel;

    public CarDto(Long id, String brand, int vehicleYear, int engineCapacity, Doors doors, Fuel fuel) {
        super(id, brand, vehicleYear, engineCapacity);
        this.doors = doors;
        this.fuel = fuel;
    }
}
