package com.mic.garage.controller;

import com.mic.garage.entity.Car;
import com.mic.garage.model.CarDto;
import com.mic.garage.service.CarServiceImpl;
import com.mic.garage.service.assembler.CarModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new vehicle")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = "application/json",
            schema = @Schema(ref = "#/components/schemas/Car"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created a new vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/CarWithId"))}),
            @ApiResponse(responseCode = "406", description = "Invalid value")
    })
    @PostMapping("/cars")
    @ResponseStatus(HttpStatus.CREATED)
    public CarDto createNewCar(@RequestBody CarDto newCar) {
        return carService.create(newCar);
    }

    @Operation(summary = "Get the vehicle list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found list of vehicles",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/getAllCar"))})
    })
    @GetMapping("/cars")
    //<CollectionModel<>>: Spring Hateoas container - it's aimed to encapsulating collection of resource
    //instead a single resource like <EntityModel<>>
    public CollectionModel<EntityModel<CarDto>> getAllCars() {
        return carService.readAll();
    }

    @Operation(summary = "Get a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/getOneCar"))}),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/cars/{id}")
    public EntityModel<CarDto> getOneCar(@Parameter(description = "id of vehicle to be searched") @PathVariable Long id) {
        return carService.readById(id);
    }

    @Operation(summary = "Modify a vehicle by its id or create one, if id doesn't found")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
            schema = @Schema(ref = "#/components/schemas/Car")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/CarWithId"))}),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PutMapping("/cars/{id}")
    public CarDto updateCar(@Parameter(description = "id of vehicle to be searched") @RequestBody CarDto newCar, @PathVariable Long id) {
        return carService.update(newCar, id);
    }

    @Operation(summary = "Delete a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Deleted vehicle"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/cars/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCar(@Parameter(description = "id of vehicle to be searched") @PathVariable Long id) {
        carService.delete(id);
    }
}
