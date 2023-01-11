package com.mic.garage.service.assembler;

import com.mic.garage.controller.MotoController;
import com.mic.garage.entity.Moto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MotoModelAssembler implements RepresentationModelAssembler<Moto, EntityModel<Moto>> {

    @Override
    public EntityModel<Moto> toModel(Moto moto) {
        return EntityModel.of(moto,
                linkTo(methodOn(MotoController.class).getOneMoto(moto.getId())).withSelfRel(),
                linkTo(methodOn(MotoController.class).getAllMoto()).withRel("moto"));
    }
}
