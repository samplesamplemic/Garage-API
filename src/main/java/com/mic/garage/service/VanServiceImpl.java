package com.mic.garage.service;

import com.mic.garage.controller.VanController;
import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Van;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.VanDto;
import com.mic.garage.repository.VanRepository;
import com.mic.garage.service.assembler.VanModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//Without @Service annotation, give back this error:
//Parameter 1 of constructor in com.mic.garage.controller.VanController required a bean of type 'com.mic.garage.service.VanServiceImpl' that could not be found.
@Service //VanDao
public class VanServiceImpl implements VehicleService<VanDto> {

    @Autowired
    private VanRepository vanRepository;
    private VanModelAssembler vanModelAssembler;

    public VanServiceImpl(VanRepository vanRepository, VanModelAssembler vanModelAssembler) {
        this.vanRepository = vanRepository;
        this.vanModelAssembler = vanModelAssembler;
    }

    @Override
    public VanDto create(VanDto vehicle) {
        Van van = new Van(vehicle.getBrand(), vehicle.getVehicleYear(), vehicle.getEngineCapacity(), vehicle.getCargoCapacity());
        vanRepository.save(van);
        return vehicle.builder()
                .id(van.getId())
                .brand(vehicle.getBrand())
                .engineCapacity(vehicle.getEngineCapacity())
                .vehicleYear(vehicle.getVehicleYear())
                .cargoCapacity(vehicle.getCargoCapacity())
                .build();
    }

    @Override
    public CollectionModel<EntityModel<VanDto>> readAll() {
        Stream<VanDto> van = vanRepository.findAll().stream().map(el -> new VanDto(el.getId(), el.getBrand(), el.getVehicleYear(), el.getEngineCapacity(), el.getCargoCapacity()));
        List<EntityModel<VanDto>> vanDto = van.map(vanModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(vanDto, linkTo(methodOn(VanController.class).getAllVan()).withRel("Van"));
    }

    @Override
    public EntityModel<VanDto> readById(Long id) {
        Van van = vanRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        VanDto vanDto = new VanDto(van.getId(), van.getBrand(), van.getVehicleYear(), van.getEngineCapacity(), van.getCargoCapacity());
        return vanModelAssembler.toModel(vanDto);
    }

    @Override
    public VanDto update(VanDto vehicle, Long id) {
        Van vanDto = vanRepository.findById(id)
                .map(van -> {
                    van.setBrand(vehicle.getBrand());
                    van.setEngineCapacity(vehicle.getEngineCapacity());
                    van.setVehicleYear(vehicle.getVehicleYear());
                    return vanRepository.save(van);
                })
                .orElseGet(() -> {
                    Van van = new Van(vehicle.getBrand(), vehicle.getVehicleYear(), vehicle.getEngineCapacity(), vehicle.getCargoCapacity());
                    vanRepository.save(van);
                    return van;
                });
        return VanDto.builder()
                .id(vanDto.getId())
                .brand(vanDto.getBrand())
                .engineCapacity(vanDto.getEngineCapacity())
                .vehicleYear(vanDto.getVehicleYear())
                .cargoCapacity(vanDto.getCargoCapacity())
                .build();
    }

    @Override
    public void delete(Long id) {
        vanRepository.deleteById(id);
    }
}

