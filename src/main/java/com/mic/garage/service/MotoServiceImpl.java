package com.mic.garage.service;

import com.mic.garage.controller.MotoController;
import com.mic.garage.entity.Moto;
import com.mic.garage.entity.Times;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.MotoDto;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.service.assembler.MotoModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MotoServiceImpl implements VehicleService<MotoDto, Moto> {

    @Autowired
    private MotoRepository motoRepository;
    private MotoModelAssembler motoModelAssembler;

    public MotoServiceImpl(MotoRepository motoRepository, MotoModelAssembler motoModelAssembler) {
        this.motoRepository = motoRepository;
        this.motoModelAssembler = motoModelAssembler;
    }

    @Override
    public MotoDto create(MotoDto vehicle) {
        Moto moto = new Moto(vehicle.getBrand(), vehicle.getEngine(), vehicle.getVehicleYear(), vehicle.getTimes());
        motoRepository.save(moto);
        return vehicle;
    }

    @Override
    public MotoDto update(MotoDto vehicle, Long id) {
        Moto motoDto = motoRepository.findById(id)
                .map(moto -> {
                    moto.setBrand(vehicle.getBrand());
                    moto.setEngine(vehicle.getEngine());
                    moto.setVehicleYear(vehicle.getVehicleYear());
                    return motoRepository.save(moto);
                })
                .orElseGet(() -> {
                    Moto moto = new Moto(vehicle.getBrand(), vehicle.getEngine(), vehicle.getVehicleYear(), vehicle.getTimes());
                    motoRepository.save(moto);
                    return moto;
                });
        return MotoDto.builder()
                .brand(motoDto.getBrand())
                .engine(motoDto.getEngine())
                .vehicleYear(motoDto.getVehicleYear())
                .times(motoDto.getTimes())
                .build();
    }

    @Override
    public CollectionModel<EntityModel<Moto>> readAll() {
        List<EntityModel<Moto>> moto = motoRepository.findAll().stream()
                .map(motoModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(moto, linkTo(methodOn(MotoController.class).getAllMoto()).withSelfRel());
    }

    @Override
    public EntityModel<Moto> readById(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return motoModelAssembler.toModel(moto);
    }

    @Override
    public void delete(Long id) {
        motoRepository.deleteById(id);
    }
}
