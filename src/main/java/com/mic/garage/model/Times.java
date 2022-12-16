package com.mic.garage.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.util.List;
import java.io.Serializable;
import java.util.stream.Stream;
//If an entity instance is to be passed by value as a detached object,
//the entity class must implement the Serializable interface.
//<Serializable> necessary to transfer entities over-the-wire
//not needed to persistence only

//@Embeddable
//@Immutable //should be use on root entities only??
//generate a private constructor to respect self-validation with factory-method
@NoArgsConstructor(staticName = "private")
public class Times implements Serializable {

    //@Column
    private int times;
    private static List<Integer> rangeTimes = List.of(2, 4);

    private Times(int times) {
        this.times = times;
    }

    //JsonValue and JsonCreator are necessary to validation between request
    @JsonValue
    public int getTimes() {
        return times;
    }

    @JsonCreator
    public static Times createTimes(int times) {
        if (rangeTimes.contains(times)) {
            return new Times(times);
        } else {
            //throw new RuntimeException("The value of times must be 2 or 4.");
            throw new VehicleArgsNotAcceptedException("The value of times must be 2 or 4.");
        }

        // return  Stream.of(rangeTimes).filter(el -> el.contains(times)).findFirst().orElseThrow(() -> new VehicleArgsNotAcceptedException("The value of times must be 2 or 4."));
    }

    @Override
    public String toString() {
        return "" + times;
    }
}

