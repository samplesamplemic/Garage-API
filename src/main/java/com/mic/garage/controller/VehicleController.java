package com.mic.garage.controller;

import com.mic.garage.model.Vehicle;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.repository.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {
    private final VehicleRepository repository;

    public VehicleController(VehicleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/vehicles")
    List<Vehicle> all(){
        return repository.findAll();
    }

    @PostMapping("/vehicles")
    Vehicle newVehicle(@RequestBody Vehicle newVehicle) {
        return repository.save(newVehicle);
    }

    @GetMapping("/vehicles/{id}")
    Vehicle one(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @PutMapping("/vehicles/{id}")
    Vehicle replaceVehicle(@RequestBody Vehicle newVehicle, @PathVariable Long id){
        return repository.findById(id)
                .map(vehicle -> {
                    vehicle.setBrand(newVehicle.getBrand());
                    vehicle.setCarYear(newVehicle.getCarYear());
                    vehicle.setCarEngine(newVehicle.getCarEngine());
                    return repository.save(vehicle);
                })
                .orElseGet(() -> {
                    newVehicle.setId(id); //this can generate an error?
                    return repository.save(newVehicle);
                });
    }

    @DeleteMapping("/vehicles/{id}")
    void deleteVehicle(@PathVariable Long id){
        repository.deleteById(id);
    }
}
