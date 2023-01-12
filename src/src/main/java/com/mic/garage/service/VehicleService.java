package com.mic.garage.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

// DAO: data access object || Structural patter
// It provides to encapsulating/isolating the details of the persistence layer and providing a CRUD interface
// to keep application/business layer completely decoupled from persistence layer
// VehicleDao
public interface VehicleService<T> {
    T create(T vehicle);

    CollectionModel<EntityModel<T>> readAll();

    EntityModel<T> readById(Long id);

    T update(T vehicle, Long id);

    void delete(Long id);
}

//T = Dto model - for command (manipulate data) operation && for query (read data) operation