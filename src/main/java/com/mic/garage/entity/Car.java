package com.mic.garage.entity;

import jakarta.persistence.*;

@Entity
public class Car extends Vehicle {

    @Embedded
    private Doors doors;

    // @Embedded
    //@Enumerated(EnumType.STRING)
    //@Column(name = "fuel")
    private Fuel fuel;

    public Car() {
    }

    public Car(String brand, int vehicleYear, int engineCapacity, Doors doors, Fuel fuel) {
        super(brand, vehicleYear, engineCapacity);
        this.doors = doors;
        this.fuel = fuel;
    }

    public int getDoors() {
        return this.doors.getDoors();
    }


//    commented cause value object principle: immutability;
//    public void setDoors(Doors doors) {
//        this.doors = doors;
//    }
//    public void setFuel(Fuel fuel) {this.fuel =fuel;}

    public Fuel getFuel() {
        return this.fuel;
    }

    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1).concat("" +
                ", doors=" + doors +
                ", fuel=" + fuel.name() +
                '}');
    }
}
