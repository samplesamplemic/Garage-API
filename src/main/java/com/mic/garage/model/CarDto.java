package com.mic.garage.model;

import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Fuel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Data
//builder pattern || creational pattern ||
//When complexity of creating object increase, it can separate the instantiation process
//by using another object(a builder) to construct the object
@SuperBuilder //to use inheritance with lombok
public class CarDto extends VehicleDto {
    private Doors doors;
    private Fuel fuel;

    public CarDto(Doors doors, Fuel fuel, String brand, int year, int carEngineCapacity) {
        super(brand, year, carEngineCapacity);
        this.doors = doors;
        this.fuel = fuel;
    }
}
