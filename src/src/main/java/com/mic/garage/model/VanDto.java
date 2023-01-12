package com.mic.garage.model;

import com.mic.garage.entity.CargoCapacity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.hateoas.server.core.Relation;

@Getter
@SuperBuilder
@Relation(collectionRelation = "van", itemRelation = "van")
public class VanDto extends VehicleDto {
    private CargoCapacity cargoCapacity;

    public VanDto(Long id,String brand, int vehicleYear, int engineCapacity, CargoCapacity cargoCapacity) {
        super(id,brand, vehicleYear, engineCapacity);
        this.cargoCapacity = cargoCapacity;
    }
}
