package com.mic.garage.model;

import com.fasterxml.jackson.annotation.JsonMerge;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
//Inheritance solution 2):
//@DiscriminatorValue("1")
public class Moto extends Vehicle {

    @Embedded
    private Times times;

    public Moto() {
    }

    public Moto(String brand, int vehicleYear, int EngineCapacity, int times) {
        super(brand, vehicleYear, EngineCapacity);
        this.times = Times.createTimes(times);
    }

    //this return a nested json
   // public Times getTimes(){return this.times;}

    //spring web use the getter to store object
    //!!!PROBLEM: value object validation bypassed!!!
    public int getTimes() {
        return Integer.valueOf(this.times.toString());
    }


    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1).concat("" +
                ", times=" + times + "\'" +
                "}");
    }
}