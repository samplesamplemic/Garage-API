package com.mic.garage.service.query;

import org.springframework.hateoas.EntityModel;

import java.util.List;

interface QueryService <T>{

    List<EntityModel<T>> getAll();

    T getById(Long id);
}
