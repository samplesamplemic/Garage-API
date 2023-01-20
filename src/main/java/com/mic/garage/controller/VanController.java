package com.mic.garage.controller;

import com.mic.garage.model.CarDto;
import com.mic.garage.model.VanDto;
import com.mic.garage.service.VanServiceImpl;
import com.mic.garage.service.assembler.VanModelAssembler;
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

@RestController
@RequestMapping("/garage")
public class VanController {

    private final VanModelAssembler vanModelAssembler;
    private final VanServiceImpl vanService;

    public VanController(VanModelAssembler vanModelAssembler, VanServiceImpl vanService) {
        this.vanModelAssembler = vanModelAssembler;
        this.vanService = vanService;
    }

    @Operation(summary = "Create a new vehicle")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(mediaType = "application/json",
            schema = @Schema(ref = "#/components/schemas/van"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created a new vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/vanWithId"))}),
            @ApiResponse(responseCode = "406", description = "Invalid value")
    })
    @PostMapping("/van")
    @ResponseStatus(HttpStatus.CREATED)
    public VanDto createNewVan(@RequestBody VanDto newVan) {
        return vanService.create(newVan);
    }

    @Operation(summary = "Get the vehicle list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found list of vehicles",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/getAllVan"))})
    })
    @GetMapping("/van")
    public CollectionModel<EntityModel<VanDto>> getAllVan() {
        return vanService.readAll();
    }

    @Operation(summary = "Get a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/getOneVan"))}),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/van/{id}")
    public EntityModel<VanDto> getOneVan(@Parameter(description = "id of vehicle to be searched") @PathVariable Long id) {
        return vanService.readById(id);
    }

    @Operation(summary = "Modify a vehicle by its id or create one, if id doesn't found")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
            schema = @Schema(ref = "#/components/schemas/van")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created vehicle",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(ref = "#/components/schemas/vanWithId"))}),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PutMapping("/van/{id}")
    public VanDto updateVan(@Parameter(description = "id of vehicle to be searched") @RequestBody VanDto newVan, @PathVariable Long id) {
        return vanService.update(newVan, id);
    }

    @Operation(summary = "Delete a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted vehicle"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/van/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteVan(@Parameter(description = "id of vehicle to be searched") @PathVariable Long id) {
        vanService.delete(id);
    }
}

