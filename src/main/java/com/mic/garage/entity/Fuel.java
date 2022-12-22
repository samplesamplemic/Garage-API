package com.mic.garage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;

import java.util.stream.Stream;

//@Embeddable
public enum Fuel {
    DIESEL("Diesel"),
    PETROL("Petrol");

    private String fuel;

    Fuel(String fuel) {
        this.fuel = fuel;
    }

    //deserialize code arrived from the RequestBody
    @JsonCreator
    public static Fuel decode(final String fuel) {
        //Stream: wrappers around a data source
        return Stream.of(Fuel.values()).filter(el -> el.fuel.equalsIgnoreCase(fuel)).findFirst().orElseThrow(() ->new VehicleArgsNotAcceptedException("Fuel must be Diesel or Petrol"));
    }

    //serialize for the get request
    @JsonValue
    public String getFuel() {
        return fuel;
    }

}
