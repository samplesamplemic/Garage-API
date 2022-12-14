package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.Moto;
import com.mic.garage.repository.MotoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("garage")
public class MotoController {
    private final MotoRepository repository;

    public MotoController(MotoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/moto")
    List<Moto> all() {
        return repository.findAll();
    }

    @PostMapping("/moto")
    Moto newMoto(@RequestBody Moto newMoto) {
        return repository.save(newMoto);
    }

    @GetMapping("/moto/{id}")
    Moto one(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

//    @PutMapping("moto/{id}")
//    Moto replaceMoto(@RequestBody Moto newMoto, @PathVariable Long id){
//        return repository.findById(id)
//                .map(moto -> {
//                    moto.setBrand(newMoto.getBrand());
//                    moto.setVehicleYear(newMoto.getVehicleYear());
//                    moto.setEngine(newMoto.getEngine());
//                    moto.setBrand(newMoto.getBrand());
//                })
//    }

    @DeleteMapping("/moto/{id}")
    void deleteMoto(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
