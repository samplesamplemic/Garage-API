package com.mic.garage.model;

import com.mic.garage.controller.CarController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarModelAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

    @Override
    public EntityModel<Car> toModel(Car car){
        return EntityModel.of(car,
                linkTo(methodOn(CarController.class).one(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).all()).withRel("cars"));
    }
}
