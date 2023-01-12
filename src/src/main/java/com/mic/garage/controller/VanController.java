package com.mic.garage.controller;

import com.mic.garage.model.VanDto;
import com.mic.garage.service.VanServiceImpl;
import com.mic.garage.service.assembler.VanModelAssembler;
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

    @PostMapping("/van")
    @ResponseStatus(HttpStatus.CREATED)
    public VanDto createNewVan(@RequestBody VanDto newVan){
        return  vanService.create(newVan);
    }

    @GetMapping("/van")
    public CollectionModel<EntityModel<VanDto>> getAllVan() {
        return vanService.readAll();
    }

    @GetMapping("/van/{id}")
    public EntityModel<VanDto> getOneVan(@PathVariable Long id) {
        return vanService.readById(id);
    }

    @PutMapping("/van/{id}")
    public VanDto updateVan(@RequestBody VanDto newVan, @PathVariable Long id){
        return vanService.update(newVan, id);
    }

    @DeleteMapping("/van/{id}")
    public void deleteVan(@PathVariable Long id){
        vanService.delete(id);
    }
}

