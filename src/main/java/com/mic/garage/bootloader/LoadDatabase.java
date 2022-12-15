package com.mic.garage.bootloader;

import com.mic.garage.model.Car;
import com.mic.garage.model.Doors;
import com.mic.garage.model.Fuel;
import com.mic.garage.model.Moto;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.repository.MotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);


    //<beans:> objects that form the backbone of application, are Spring IoC container;
    //this object delegates the job of constructing such dependencies to an IoC container;
    //IoC(inversion of Control: process in which an object defines its dependency without creating them)
    @Bean
    CommandLineRunner initDatabase(MotoRepository motoRepository, CarRepository carRepository) {
        return args -> {

            carRepository.save(new Car(Doors.createDoors(3), Fuel.DIESEL, "Alfa Romeo", 2011, 1300));
            motoRepository.save(new Moto("Kawasaki", 2013, 30, 4));

            for (var moto : motoRepository.findAll()) {
                log.info(moto.toString());
            }
            for (var car : carRepository.findAll()) {
                log.info(car.toString());
            }
        };
    }
}
