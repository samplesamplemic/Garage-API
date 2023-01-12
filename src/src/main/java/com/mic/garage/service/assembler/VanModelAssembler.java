package com.mic.garage.service.assembler;

import com.mic.garage.controller.VanController;
import com.mic.garage.model.VanDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//Without @Component annotation, give back this error:
//Parameter 0 of constructor in com.mic.garage.controller.VanController required a bean of type 'com.mic.garage.service.assembler.VanModelAssembler' that could not be found.
@Component
public class VanModelAssembler implements RepresentationModelAssembler<VanDto, EntityModel<VanDto>> {
    @Override
    public EntityModel<VanDto> toModel(VanDto van) {
        return EntityModel.of(van,
                linkTo(methodOn(VanController.class).getOneVan(van.getId())).withSelfRel(),
                linkTo(methodOn(VanController.class).getAllVan()).withRel("van"));
    }
}
