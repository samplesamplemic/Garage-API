package com.mic.garage.service.command;

import com.mic.garage.entity.Car;
import com.mic.garage.model.CarDto;
import com.mic.garage.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Car_command_service")
public class CommandCarServiceImpl implements CommandCarService{


    @Autowired
    private CarRepository carRepository;


    @Override
    public CarDto create(CarDto vehicle) {
        //mapper da CarDTO a Car
        Car car = new Car();
        car.setBrand(vehicle.getBrand());
        car.setEngine(vehicle.getEngine());
        car.setVehicleYear(vehicle.getVehicleYear());
        carRepository.save(car);
        return vehicle;
    }

    @Override
    public CarDto modify(CarDto vehicle,Long id) {
        var carDto =  carRepository.findById(id)
                .map(car -> {
                    car.setBrand(vehicle.getBrand());
                    car.setEngine(vehicle.getEngine());
                    car.setVehicleYear(vehicle.getVehicleYear());
                    return carRepository.save(car);
                })
                .orElseGet(() ->{
                            Car car = new Car();
                            car.setBrand(vehicle.getBrand());
                            car.setEngine(vehicle.getEngine());
                            car.setVehicleYear(vehicle.getVehicleYear());
                            carRepository.save( car);
                            return car;
                        }
                        );
        return CarDto.builder().brand(carDto.getBrand())
                .engine(carDto.getEngine())
                .vehicleYear(carDto.getVehicleYear())
                .build();
    }

    @Override
    public void delete(Long id) {
        carRepository.deleteById(id);
    }

}
