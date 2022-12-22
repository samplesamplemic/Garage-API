package com.mic.garage.service.query;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.entity.Car;
import com.mic.garage.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("QueryCarService")
public class QueryCarServiceImpl implements QueryCarService{


    private final CarRepository carRepository;

    public QueryCarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAll() {
        return carRepository.findAll();
    }

    @Override
    public Car getById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }
}
