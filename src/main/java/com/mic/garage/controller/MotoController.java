package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.entity.Moto;
import com.mic.garage.repository.MotoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("garage")
public class MotoController {
    private final MotoRepository motoRepository;

    public MotoController(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    @GetMapping("/moto")
    List<Moto> all() {
        return motoRepository.findAll();
    }

    @PostMapping("/moto")
    Moto newMoto(@RequestBody Moto newMoto) {
        return motoRepository.save(newMoto);
    }

    @GetMapping("/moto/{id}")
    Moto one(@PathVariable Long id) {
        return motoRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @PutMapping("moto/{id}")
    Moto replaceMoto(@RequestBody Moto newMoto, @PathVariable Long id) {
        return motoRepository.findById(id)
                .map(moto -> {
                    moto.setBrand(newMoto.getBrand());
                    moto.setVehicleYear(newMoto.getVehicleYear());
                    moto.setEngine(newMoto.getEngine());
                    //not set <times> to respect value object immutability
                    return motoRepository.save(moto);
                })
                .orElseGet(() -> {
                    newMoto.setId(id);
                    return motoRepository.save(newMoto);
                });
    }

    @DeleteMapping("/moto/{id}")
    void deleteMoto(@PathVariable Long id) {
        motoRepository.deleteById(id);
    }

}
