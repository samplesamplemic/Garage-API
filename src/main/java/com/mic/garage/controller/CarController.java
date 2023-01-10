package com.mic.garage.controller;

import com.mic.garage.entity.Car;
import com.mic.garage.model.CarDto;
import com.mic.garage.service.CarServiceImpl;
import com.mic.garage.service.assembler.CarModelAssembler;
import com.mic.garage.service.command.CommandCarService;
import com.mic.garage.service.query.QueryCarService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("garage")
public class CarController {

    private final CarModelAssembler carModelAssembler;
    private final CarServiceImpl carService;

    public CarController(CarModelAssembler carModelAssembler, CarServiceImpl carService) {
        this.carModelAssembler = carModelAssembler;
        this.carService = carService;
    }

//    private final QueryCarService queryCarService;
//    private final CommandCarService commandService;
//         ||| Command - Query Pattern |||
//    //@Qualifier annotation to indicate which bean we want to use
//    //used with @Service
//    public CarController(@Qualifier(value = "Car_command_service") CommandCarService commandService, @Qualifier(value = "QueryCarService2") QueryCarService queryCarService, CarModelAssembler carModelAssembler) {
//        this.commandService = commandService;
//        this.queryCarService = queryCarService;
//        this.carModelAssembler = carModelAssembler;
//    }


    @GetMapping("/cars")
    //<CollectionModel<>>: Spring Hateoas container - it's aimed to encapsulating collection of resource
    //instead a single resource like <EntityModel<>>
    public CollectionModel<EntityModel<Car>> getAllCars() {
        var cars = carService.readAll();
        return CollectionModel.of(cars, linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
    }


    @PostMapping("cars")
    public CarDto createNewCar(@RequestBody CarDto newCar) {
        return carService.create(newCar);
    }

    @GetMapping("cars/{id}")
    public EntityModel<Car> getOneCar(@PathVariable Long id) {
        Car car = carService.readById(id);
        return carModelAssembler.toModel(car);
    }


    @PutMapping("/cars/{id}")
    public CarDto updateCar(@RequestBody CarDto newCar, @PathVariable Long id) {
        return carService.update(newCar, id);
    }

    @DeleteMapping("/cars/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.delete(id);
    }
}
