package com.mic.garage.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

//@Embeddable
public enum Fuel {
    DIESEL("Diesel"),
    PETROL("Petrol");

    private String fuel;

    Fuel(String fuel){
        this.fuel = fuel;
    }

    //deserialize code arrived from the RequestBody
    @JsonCreator
    public static Fuel decode(final String fuel){
        return Stream.of(Fuel.values()).filter(el -> el.fuel.equals(fuel)).findFirst().orElse(null);
    }

    //serialize for the get request
    @JsonValue
    public String getFuel(){
        return fuel;
    }

}
