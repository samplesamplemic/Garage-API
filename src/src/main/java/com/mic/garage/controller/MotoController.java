package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.entity.Moto;
import com.mic.garage.model.MotoDto;
import com.mic.garage.service.MotoServiceImpl;
import com.mic.garage.service.assembler.MotoModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("garage")
public class MotoController {

    private final MotoModelAssembler motoModelAssembler;
    private final MotoServiceImpl motoService;

    public MotoController(MotoModelAssembler motoModelAssembler, MotoServiceImpl motoService) {
        this.motoModelAssembler = motoModelAssembler;
        this.motoService = motoService;
    }

    @PostMapping("/moto")
    @ResponseStatus(HttpStatus.CREATED)
    public MotoDto createNewMoto(@RequestBody MotoDto newMoto) {
        return motoService.create(newMoto);
    }

    @GetMapping("/moto")
    public CollectionModel<EntityModel<MotoDto>> getAllMoto() {
        return motoService.readAll();
    }

    @GetMapping("/moto/{id}")
    public EntityModel<MotoDto> getOneMoto(@PathVariable Long id) {
        return motoService.readById(id);
    }

    @PutMapping("/moto/{id}")
    public MotoDto updateMoto(@RequestBody MotoDto newMoto, @PathVariable Long id) {
        return motoService.update(newMoto, id);
    }

    @DeleteMapping("/moto/{id}")
    public void deleteMoto(@PathVariable Long id) {
        motoService.delete(id);
    }

}
