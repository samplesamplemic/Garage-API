package com.mic.garage.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

// DAO: data access object || Structural patter
// It provides to encapsulating the details of the persistence layer and providing a CRUD interface
// to keep domain model/business logic completely decoupled from persistence layer
public interface VehicleService<T, S> {
    T create(T vehicle);

    T update(T vehicle, Long id);

    CollectionModel<EntityModel<S>> readAll();

    EntityModel<S> readById(Long id);

    void delete(Long id);
}

//T = Dto model - for command (manipulate data) operation || S = Entity model - for query (read data) operation