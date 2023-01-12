package com.mic.garage.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class Van extends Vehicle {

    @Embedded
    private CargoCapacity cargoCapacity;

    public Van() {
    }

    public Van(String brand, int vehicleYear, int engineCapacity, CargoCapacity cargoCapacity) {
        super(brand, vehicleYear, engineCapacity);
        this.cargoCapacity = cargoCapacity;
    }

    public CargoCapacity getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1).concat("" +
                ", cargoCapacity=" + cargoCapacity + "kg" +
                '}');
    }
}