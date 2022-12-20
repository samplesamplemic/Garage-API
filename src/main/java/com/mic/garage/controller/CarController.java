package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.Car;
import com.mic.garage.model.CarModelAssembler;
import com.mic.garage.repository.CarRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("garage")
public class CarController {

    private final CarRepository carRepository;

    private final CarModelAssembler assembler;

    public CarController(CarRepository carRepository, CarModelAssembler assembler) {
        this.carRepository = carRepository;
        this.assembler = assembler;
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
//   public  CollectionModel<EntityModel<Car>> all() {
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
    public CollectionModel<EntityModel<Car>> all(){
        List<EntityModel<Car>> cars = carRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(cars, linkTo(methodOn(CarController.class).all()).withSelfRel());
    }



    //deserialize
    @PostMapping("cars")
    Car newCar(@RequestBody Car newCar) {
        return carRepository.save(newCar);
    }

    //1)
//    @GetMapping("/cars/{id}")
//    Car one(@PathVariable Long id) {
//        return carRepository.findById(id)
//                .orElseThrow(() -> new VehicleNotFoundException(id));
//    }

    //2)
    // more restful api - importing <hateoas> dependency
//    @GetMapping("cars/{id}")
//    EntityModel<Car> one(@PathVariable Long id) {
//        Car car = carRepository.findById(id)
//                //lambda expression
//                .orElseThrow(() -> new VehicleNotFoundException(id));
//
//        return EntityModel.of(car,
//                linkTo(methodOn(CarController.class).one(id)).withSelfRel(),
//                linkTo(methodOn(CarController.class).all()).withRel("cars"));
//    }

    //3)
    @GetMapping("cars/{id}")
   public EntityModel<Car> one(@PathVariable Long id){
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        return assembler.toModel(car);
    }


    @PutMapping("/cars/{id}")
    Car replaceCar(@RequestBody Car newCar, @PathVariable Long id) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setBrand(newCar.getBrand());
                    car.setEngine(newCar.getEngine());
                    car.setVehicleYear(newCar.getVehicleYear());
                    return carRepository.save(car);
                })
                .orElseGet(() -> {
                    newCar.setId(id);
                    return carRepository.save(newCar);
                });
    }

    @DeleteMapping("/cars/{id}")
    void deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
    }
}
