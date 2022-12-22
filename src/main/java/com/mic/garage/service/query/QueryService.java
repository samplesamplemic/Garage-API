package com.mic.garage.service.query;

import java.util.List;

interface QueryService <T>{

    List<T> getAll();

    T getById(Long id);
}
