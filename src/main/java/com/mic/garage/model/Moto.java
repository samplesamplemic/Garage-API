package com.mic.garage.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Moto extends Vehicle {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    @Embedded
    private Times times;


    public Moto() {
    }

    public Moto(String brand, int vehicleYear, int EngineCapacity, Times times) {
        super(brand, vehicleYear, EngineCapacity);
        this.times = times;
    }

    public Times getTimes() {
        return this.times;
    }

    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1).concat("," +
                "times= "+times+
                '}');
    }
}