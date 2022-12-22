package com.mic.garage.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDto {
    private String brand;
    private int vehicleYear;
    private int engine;

}
