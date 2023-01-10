package com.mic.garage.service.assembler;

import com.mic.garage.controller.CarController;
import com.mic.garage.entity.Car;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//RepresentationalModel: base class for DTOs to collect links
//RepresentationModelAssembler: to convert a domain type into a RepresentationModel - method <toModel(T entity)>
@Component
public class CarModelAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

    @Override
    public EntityModel<Car> toModel(Car car){
        return EntityModel.of(car,
                linkTo(methodOn(CarController.class).getOneCar(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).getAllCars()).withRel("cars"));
    }
}
