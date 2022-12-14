package com.mic.garage.model;


import jakarta.persistence.*;

import java.io.Serializable;

//@Entity
//@Table(name = "Vehicles")
@MappedSuperclass
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private int vehicleYear;
    private int engine;

    public Vehicle() {
    }

    public Vehicle(String brand, int vehicleYear, int engine) {
        this.vehicleYear = vehicleYear;
        this.engine = engine;
        this.brand = brand;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(int vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public int getEngine() {
        return engine;
    }

    public void setEngine(int engine) {
        this.engine = engine;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", year=" + vehicleYear +
                ", engine capacity=" + engine +
                ", brand='" + brand + '\'' +
                '}';
    }
}
