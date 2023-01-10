package com.mic.garage.service.query;

import com.mic.garage.entity.Car;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("QueryCarService2")
public class QueryCarService2Impl implements QueryCarService{

    private final CarRepository carRepository;
    private final CarModelAssembler carModelAssembler;

    public QueryCarService2Impl(CarRepository carRepository, CarModelAssembler carModelAssembler) {
        this.carRepository = carRepository;
        this.carModelAssembler = carModelAssembler;
    }
    @Override
    public List<EntityModel<Car>> getAll() {
        return carRepository.findAll().stream()
                .map(carModelAssembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Car getById(Long id) {
        System.out.println("CIAO");
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        //Aggiundo la chiamata API

        return car;
    }
}
