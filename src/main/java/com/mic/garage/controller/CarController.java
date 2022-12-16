package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
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

    @GetMapping("/cars/{id}")
    Car one(@PathVariable Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @PutMapping("/cars/{id}")
    Car replaceCar(@RequestBody Car newCar, @PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setBrand(newCar.getBrand());
                    car.setEngine(newCar.getEngine());
                    car.setVehicleYear(newCar.getVehicleYear());
                    return carRepository.save(car);
                })
                .orElseGet(() -> {
                    newCar.setId(id);
                    return carRepository.save(newCar);
                });
    }

    @DeleteMapping("/cars/{id}")
    void deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
    }
}
