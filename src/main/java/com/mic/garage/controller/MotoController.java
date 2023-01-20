package com.mic.garage.controller;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.entity.Moto;
import com.mic.garage.model.CarDto;
import com.mic.garage.model.MotoDto;
import com.mic.garage.service.MotoServiceImpl;
import com.mic.garage.service.assembler.MotoModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("garage")
public class MotoController {

    private final MotoModelAssembler motoModelAssembler;
    private final MotoServiceImpl motoService;

    public MotoController(MotoModelAssembler motoModelAssembler, MotoServiceImpl motoService) {
        this.motoModelAssembler = motoModelAssembler;
        this.motoService = motoService;
    }

    @Operation(summary = "Create a new vehicle")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = "application/json",
            schema = @Schema(ref = "#/components/schemas/moto"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created a new vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/motoWithId"))}),
            @ApiResponse(responseCode = "406", description = "Invalid value")
    })
    @PostMapping("/moto")
    @ResponseStatus(HttpStatus.CREATED)
    public MotoDto createNewMoto(@RequestBody MotoDto newMoto) {
        return motoService.create(newMoto);
    }

    //------------------------------------------------
    // attempt to add another media type format for requests - BUGGED
    //@PostMapping(path = "/moto", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    //@ResponseStatus(HttpStatus.CREATED)
    //ERROR - Cannot convert value of type 'java.lang.String' to required type 'com.mic.garage.entity.Times':
    // no matching editors or conversion strategy found - the rest work correctly
    //public MotoDto createNewMotoFormUrlEncoded(MotoDto newMoto) {
     //   return motoService.create(newMoto);}
    //--------------------------------------------

    @Operation(summary = "Get the vehicle list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found list of vehicles",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/getAllMoto"))})
    })
    @GetMapping("/moto")
    public CollectionModel<EntityModel<MotoDto>> getAllMoto() {
        return motoService.readAll();
    }

    @Operation(summary = "Get a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/getOneMoto"))}),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/moto/{id}")
    public EntityModel<MotoDto> getOneMoto(@Parameter(description = "id of vehicle to be searched") @PathVariable Long id) {
        return motoService.readById(id);
    }

    @Operation(summary = "Modify a vehicle by its id or create one, if id doesn't found")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
            schema = @Schema(ref = "#/components/schemas/moto")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/motoWithId"))}),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PutMapping("/moto/{id}")
    public MotoDto updateMoto(@Parameter(description = "id of vehicle to be searched") @RequestBody MotoDto newMoto, @PathVariable Long id) {
        return motoService.update(newMoto, id);
    }

    @Operation(summary = "Delete a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted vehicle"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/moto/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteMoto(@Parameter(description = "id of vehicle to be searched") @PathVariable Long id) {
        motoService.delete(id);
    }

}
