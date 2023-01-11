package com.mic.garage.service;

import com.mic.garage.controller.CarController;
import com.mic.garage.entity.Car;
import com.mic.garage.entity.Doors;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.CarDto;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CarServiceImpl implements VehicleService<CarDto, Car> {

    @Autowired
    private CarRepository carRepository;
    private CarModelAssembler carModelAssembler;

    public CarServiceImpl(CarRepository carRepository, CarModelAssembler carModelAssembler) {
        this.carRepository = carRepository;
        this.carModelAssembler = carModelAssembler;
    }

    @Override
    public CollectionModel<EntityModel<Car>> readAll() {
        List<EntityModel<Car>> cars = carRepository.findAll().stream()
                .map(carModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(cars, linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
    }

    @Override
    public EntityModel<Car> readById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return carModelAssembler.toModel(car);
    }

    @Override
    public CarDto create(CarDto vehicle) {
        Car car = new Car(vehicle.getDoors(), vehicle.getFuel(), vehicle.getBrand(), vehicle.getEngine(), vehicle.getVehicleYear());
        carRepository.save(car);
        return vehicle;
    }

    @Override
    public CarDto update(CarDto vehicle, Long id) {
        var carDto = carRepository.findById(id)
                .map(car -> {
                    car.setBrand(vehicle.getBrand());
                    car.setEngine(vehicle.getEngine());
                    car.setVehicleYear(vehicle.getVehicleYear());
                    return carRepository.save(car);
                })
                .orElseGet(() -> {
                    Car car = new Car(vehicle.getDoors(), vehicle.getFuel(), vehicle.getBrand(), vehicle.getEngine(), vehicle.getVehicleYear());
                    carRepository.save(car);
                    return car;
                });
        return CarDto.builder()
                .brand(carDto.getBrand())
                .engine(carDto.getEngine())
                .vehicleYear(carDto.getVehicleYear())
                .fuel(carDto.getFuel())
                .doors(Doors.createDoors(carDto.getDoors()))
                .build();
    }

    @Override
    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}
