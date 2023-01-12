package com.mic.garage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "private")
@Embeddable
public class CargoCapacity {

    private int cargoCapacity;

    private CargoCapacity(int cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    @JsonCreator
    public static CargoCapacity createCargoCapacity(int cargoCapacity) {
        if (cargoCapacity >= 0) {
            return new CargoCapacity(cargoCapacity);
        } else {
            // throw new RuntimeException("The value of cargo Capacity must be positive.");
            throw new VehicleArgsNotAcceptedException("The value of cargo Capacity must be positive.");
        }
    }

    @JsonValue
    public int getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public String toString() {
        return "" + cargoCapacity;
    }

}
