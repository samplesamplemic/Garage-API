package com.mic.garage.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

// DAO: data access object || Structural patter
// It provides to encapsulating the details of the persistence layer and providing a CRUD interface
// to keep domain model/business logic completely decoupled from persistence layer
public interface VehicleService<T> {
    T create(T vehicle);

    CollectionModel<EntityModel<T>> readAll();

    EntityModel<T> readById(Long id);

    T update(T vehicle, Long id);

    void delete(Long id);
}

//T = Dto model - for command (manipulate data) operation || S = Entity model - for query (read data) operation