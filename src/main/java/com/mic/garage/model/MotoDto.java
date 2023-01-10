package com.mic.garage.model;

import com.mic.garage.entity.Times;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MotoDto extends VehicleDto {
    private Times times;

    public MotoDto(String brand, int vehicleYear, int engine, Times times) {
        super(brand, vehicleYear, engine);
        this.times = times;
    }
}
