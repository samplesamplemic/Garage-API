package com.mic.garage.controller;

import com.mic.garage.entity.Car;
import com.mic.garage.model.CarDto;
import com.mic.garage.service.CarServiceImpl;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/cars")
    //<CollectionModel<>>: Spring Hateoas container - it's aimed to encapsulating collection of resource
    //instead a single resource like <EntityModel<>>
    public CollectionModel<EntityModel<CarDto>> getAllCars() {
        return carService.readAll();
    }

    @GetMapping("cars/{id}")
    public EntityModel<CarDto> getOneCar(@PathVariable Long id) {
        return carService.readById(id);
    }

    @PostMapping("cars")
    @ResponseStatus(HttpStatus.CREATED)
    public CarDto createNewCar(@RequestBody CarDto newCar) {
        return carService.create(newCar);
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
