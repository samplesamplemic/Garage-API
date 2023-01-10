package com.mic.garage.service.query;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.entity.Car;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@Service: to mark class business logic
@Service("QueryCarService")
public class QueryCarServiceImpl implements QueryCarService{


    private final CarRepository carRepository;
    private final CarModelAssembler carModelAssembler;

    public QueryCarServiceImpl(CarRepository carRepository, CarModelAssembler carModelAssembler) {
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
        return carRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }
}
