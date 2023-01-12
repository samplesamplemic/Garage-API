package com.mic.garage.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@NoArgsConstructor(staticName = "private")
public class Doors {

    private int doors;
    private static List<Integer> rangeDoors = List.of(3, 5);

    //value object principle: self-validation
    //but validation in constructor break patter: separation of concern
    //solution: factory method?
    private Doors(int doors) {
        this.doors = doors;
    }

    @JsonValue
    public int getDoors() {
        return doors;
    }

    //factory method
    @JsonCreator
    public static Doors createDoors(int doors) {
        if (rangeDoors.contains(doors)) {
            return new Doors(doors);
        } else {
            throw new VehicleArgsNotAcceptedException("The value of doors must be between 3 or 5.");
        }
    }

    public String toString() {
        return ""+doors;
    }
}
