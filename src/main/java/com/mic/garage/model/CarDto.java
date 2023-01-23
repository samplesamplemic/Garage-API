package com.mic.garage.model;

import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Fuel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.hateoas.server.core.Relation;

@Getter
@SuperBuilder //to use inheritance with lombok
@Relation(collectionRelation = "cars", itemRelation = "cars") //hateoas embedded name
public class CarDto extends VehicleDto {
    private Doors doors;
    private Fuel fuel;

    public CarDto(Long id, String brand, int vehicleYear, int engineCapacity, Doors doors, Fuel fuel) {
        super(id, brand, vehicleYear, engineCapacity);
        this.doors = doors;
        this.fuel = fuel;
    }
}
