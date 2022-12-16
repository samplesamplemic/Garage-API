package com.mic.garage.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class Van extends Vehicle {

    @Embedded
    private CargoCapacity cargoCapacity;

    public Van() {
    }

    public Van(String brand, int year, int carEngineCapacity, CargoCapacity cargoCapacity) {
        super(brand, year, carEngineCapacity);
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