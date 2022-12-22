package com.mic.garage.controller;

import com.mic.garage.entity.Car;
import com.mic.garage.model.CarDto;
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


    private final CommandCarService commandService;

    private final QueryCarService queryCarService;

    private final CarModelAssembler carModelAssembler;

    public CarController(@Qualifier(value = "Car_command_service")CommandCarService commandService, @Qualifier(value = "QueryCarService2") QueryCarService queryCarService, CarModelAssembler carModelAssembler) {
        this.commandService = commandService;
        this.queryCarService = queryCarService;
        this.carModelAssembler = carModelAssembler;
    }

    //1)
//    @GetMapping("/cars")
//    //method default: allow access only from inside package
//    List<Car> all() {
//        return carRepository.findAll();
//    }

    //2)
    // @GetMapping("/cars")
    //<CollectionModel<>>: Spring Hateoas container - it's aimed to encapsulating collection of resource
    //instead a single resource like <EntityModel<>>
//   CollectionModel<EntityModel<Car>> all() {
//        List<EntityModel<Car>> cars = carRepository.findAll().stream()
//                .map(car -> EntityModel.of(car,
//                        linkTo(methodOn(CarController.class).one(car.getId())).withSelfRel(),
//                        linkTo(methodOn(CarController.class).all()).withRel("cars")))
//                .collect(Collectors.toList());
//
//        return CollectionModel.of(cars, linkTo(methodOn(CarController.class).all()).withSelfRel());
//    }

    //3)
    @GetMapping("/cars")
    public CollectionModel<Car> all() {
        var cars = queryCarService.getAll();
        return CollectionModel.of(CollectionModel.of(cars), linkTo(methodOn(CarController.class).all()).withSelfRel());
    }


    @PostMapping("cars")
    public CarDto newCar(@RequestBody CarDto newCar) {
        return commandService.create(newCar);
    }

    //3)
    @GetMapping("cars/{id}")
    public EntityModel<Car> one(@PathVariable Long id) {
        Car car = queryCarService.getById(id);
        return carModelAssembler.toModel(car);
    }


    @PutMapping("/cars/{id}")
    public CarDto replaceCar(@RequestBody CarDto newCar, @PathVariable Long id) {
        return commandService.modify(newCar,id);
    }

    @DeleteMapping("/cars/{id}")
    public void deleteCar(@PathVariable Long id) {
        commandService.delete(id);
    }
}
