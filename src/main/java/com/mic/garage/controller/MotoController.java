package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.entity.Moto;
import com.mic.garage.model.MotoDto;
import com.mic.garage.service.MotoServiceImpl;
import com.mic.garage.service.assembler.MotoModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(path = "/moto", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public MotoDto createNewMoto(@RequestBody MotoDto newMoto) {
        return motoService.create(newMoto);
    }

    @PostMapping(path = "/moto", consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.CREATED)
    //ERROR - Cannot convert value of type 'java.lang.String' to required type 'com.mic.garage.entity.Times':
    // no matching editors or conversion strategy found - the rest work correctly
    public MotoDto createNewMotoFormUrlEncoded(MotoDto newMoto) {
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteMoto(@PathVariable Long id) {
        motoService.delete(id);
    }

}
