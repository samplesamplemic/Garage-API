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
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CarServiceImpl implements VehicleService<CarDto> {

    @Autowired
    private CarRepository carRepository;
    private CarModelAssembler carModelAssembler;

    public CarServiceImpl(CarRepository carRepository, CarModelAssembler carModelAssembler) {
        this.carRepository = carRepository;
        this.carModelAssembler = carModelAssembler;
    }

    @Override
    public CollectionModel<EntityModel<CarDto>> readAll() {
        Stream<CarDto> cars = carRepository.findAll().stream()
                .map(car -> new CarDto(car.getId(), car.getBrand(), car.getVehicleYear(), car.getEngineCapacity(), Doors.createDoors(car.getDoors()), car.getFuel()));
        List<EntityModel<CarDto>> carsDto = cars.map(carModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(carsDto, linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
    }

    @Override
    public EntityModel<CarDto> readById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        CarDto carDto = new CarDto(car.getId(), car.getBrand(), car.getVehicleYear(), car.getEngineCapacity(), Doors.createDoors(car.getDoors()), car.getFuel());
        return carModelAssembler.toModel(carDto);
    }

    @Override
    public CarDto create(CarDto vehicle) {
        Car car = new Car(vehicle.getBrand(), vehicle.getVehicleYear(), vehicle.getEngineCapacity(), vehicle.getDoors(), vehicle.getFuel());
        carRepository.save(car);
        return vehicle.builder()
                .brand(vehicle.getBrand())
                .vehicleYear(vehicle.getVehicleYear())
                .engineCapacity(vehicle.getEngineCapacity())
                .doors(vehicle.getDoors())
                .fuel(vehicle.getFuel())
                .build();
    }

    @Override
    public CarDto update(CarDto vehicle, Long id) {
        var carDto = carRepository.findById(id)
                .map(car -> {
                    car.setBrand(vehicle.getBrand());
                    car.setEngineCapacity(vehicle.getEngineCapacity());
                    car.setVehicleYear(vehicle.getVehicleYear());
                    return carRepository.save(car);
                })
                .orElseGet(() -> {
                    Car car = new Car(vehicle.getBrand(), vehicle.getVehicleYear(), vehicle.getEngineCapacity(), vehicle.getDoors(), vehicle.getFuel());
                    carRepository.save(car);
                    return car;
                });
        return CarDto.builder()
                .brand(carDto.getBrand())
                .engineCapacity(carDto.getEngineCapacity())
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
