package com.mic.garage.controller;

import com.mic.garage.model.Car;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.repository.MotoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("garage")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping("/cars")
    List<Car> all() {
        return carRepository.findAll();
    }

    //deserialize
    @PostMapping("cars")
    Car newCar(@RequestBody Car newCar) {
        return carRepository.save(newCar);
    }
}
