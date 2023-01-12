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
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service // MotoDao
public class MotoServiceImpl implements VehicleService<MotoDto> {

    @Autowired
    private MotoRepository motoRepository;
    private MotoModelAssembler motoModelAssembler;

    public MotoServiceImpl(MotoRepository motoRepository, MotoModelAssembler motoModelAssembler) {
        this.motoRepository = motoRepository;
        this.motoModelAssembler = motoModelAssembler;
    }

    @Override
    public MotoDto create(MotoDto vehicle) {
        Moto moto = new Moto(vehicle.getBrand(), vehicle.getVehicleYear(), vehicle.getEngineCapacity(), vehicle.getTimes());
        motoRepository.save(moto);
        return vehicle.builder()
                .id(moto.getId())
                .brand(vehicle.getBrand())
                .engineCapacity(vehicle.getEngineCapacity())
                .vehicleYear(vehicle.getVehicleYear())
                .times(vehicle.getTimes())
                .build();
    }

    @Override
    public CollectionModel<EntityModel<MotoDto>> readAll() {
        Stream<MotoDto> moto = motoRepository.findAll().stream()
                .map(el -> new MotoDto(el.getId(), el.getBrand(), el.getVehicleYear(), el.getEngineCapacity(), el.getTimes()));
        List<EntityModel<MotoDto>> motoDto = moto.map(motoModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(motoDto, linkTo(methodOn(MotoController.class).getAllMoto()).withSelfRel());
    }

    @Override
    public EntityModel<MotoDto> readById(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        MotoDto motoDto = new MotoDto(moto.getId(), moto.getBrand(), moto.getVehicleYear(), moto.getEngineCapacity(), moto.getTimes());
        return motoModelAssembler.toModel(motoDto);
    }

    @Override
    public MotoDto update(MotoDto vehicle, Long id) {
        Moto motoDto = motoRepository.findById(id)
                .map(moto -> {
                    moto.setBrand(vehicle.getBrand());
                    moto.setEngineCapacity(vehicle.getEngineCapacity());
                    moto.setVehicleYear(vehicle.getVehicleYear());
                    return motoRepository.save(moto);
                })
                .orElseGet(() -> {
                    Moto moto = new Moto(vehicle.getBrand(), vehicle.getVehicleYear(), vehicle.getEngineCapacity(), vehicle.getTimes());
                    motoRepository.save(moto);
                    return moto;
                });
        return MotoDto.builder()
                .id(motoDto.getId())
                .brand(motoDto.getBrand())
                .engineCapacity(motoDto.getEngineCapacity())
                .vehicleYear(motoDto.getVehicleYear())
                .times(motoDto.getTimes())
                .build();
    }

    @Override
    public void delete(Long id) {
        motoRepository.deleteById(id);
    }
}
