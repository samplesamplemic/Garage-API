package com.mic.garage.service.query;

import com.mic.garage.entity.Car;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("QueryCarService2")
public class QueryCarService2Impl implements QueryCarService{

    private final CarRepository carRepository;

    public QueryCarService2Impl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAll() {
        return carRepository.findAll();
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
