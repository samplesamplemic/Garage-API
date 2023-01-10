package com.mic.garage.model;

import com.mic.garage.entity.CargoCapacity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class VanDto extends VehicleDto {
    private CargoCapacity cargoCapacity;

    public VanDto(String brand, int vehicleYear, int engine, CargoCapacity cargoCapacity) {
        super(brand, vehicleYear, engine);
        this.cargoCapacity = cargoCapacity;
    }
}
