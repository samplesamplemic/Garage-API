package com.mic.garage.entity;

import jakarta.persistence.*;

@Entity
//Inheritance solution 2):
//@DiscriminatorValue("1")
public class Moto extends Vehicle {

   // @Embedded
    private Times times;

    public Moto() {
    }

    public Moto(String brand, int vehicleYear, int engineCapacity, Times times) {
        super(brand, vehicleYear, engineCapacity);
        this.times = times;
    }

    //this return a nested json
   // public Times getTimes(){return this.times;}

    //spring web use the getter to store object
    //!!!PROBLEM: value object validation bypassed!!!
    //solution: in value object(class) -> @JsonValue to getVar() and @JsonCreator to factory method, or in general the validation method
    //this also resolve the nested object json;
    public Times getTimes() {
        return times;
    }


    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1).concat("" +
                ", times=" + times + "\'" +
                "}");
    }
}