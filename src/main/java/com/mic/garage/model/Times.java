package com.mic.garage.model;

import java.util.List;

public class Times {

    private int times;
    private static List<Integer> rangeTimes = List.of(2, 4);

    private Times(int times) {
        this.times = times;
    }


    public static Times createTimes(int times) {
        if (rangeTimes.contains(times)) {
            return new Times(times);
        } else {
            throw new RuntimeException("The value of times must be 2 or 4.");
        }
    }

    public String toString(){
        return String.valueOf(times);
    }
}

