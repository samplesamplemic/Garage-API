package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.Van;
import com.mic.garage.repository.VanRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garage")
public class VanController {
    private final VanRepository vanRepository;

    public VanController(VanRepository vanRepository) {
        this.vanRepository = vanRepository;
    }

    @GetMapping("/vans")
    List<Van> all() {
        return vanRepository.findAll();
    }

    @PostMapping("/vans")
    Van newVan(@RequestBody Van newVan) {
        return vanRepository.save(newVan);
    }

    @GetMapping("/vans/{id}")
    Van one(@PathVariable Long id) {
        return vanRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @PutMapping("/vans/{id}")
    Van replaceVan(@RequestBody Van newVan, @PathVariable Long id) {
        return vanRepository.findById(id)
                .map(van -> {
                    van.setBrand(newVan.getBrand());
                    van.setEngine(newVan.getEngine());
                    van.setVehicleYear(newVan.getVehicleYear());
                    return vanRepository.save(van);
                })
                .orElseGet(() -> {
                    newVan.setId(id);
                    return vanRepository.save(newVan);
                });
    }

    @DeleteMapping("/vans/{id}")
    void deleteVan(@PathVariable Long id) {
        vanRepository.deleteById(id);
    }
}

