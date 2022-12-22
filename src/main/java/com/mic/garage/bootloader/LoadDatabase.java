package com.mic.garage.bootloader;

import com.mic.garage.entity.*;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.repository.VanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

@Configuration
public class LoadDatabase {

    //NON é UNA CONFIGURATION.

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);


    //<beans:> objects that form the backbone of application, are Spring IoC container;
    //this object delegates the job of constructing such dependencies to an IoC container;
    //IoC(inversion of Control: process in which an object defines its dependency without creating them)
    @Bean
    CommandLineRunner initDatabase(MotoRepository motoRepository, CarRepository carRepository, VanRepository vanRepository) {
        return args -> {
            //Liskov substitution
            Vehicle v = new Moto("Honda", 2010, 125, Times.createTimes(2));

            carRepository.save(new Car(Doors.createDoors(3), Fuel.DIESEL, "Alfa Romeo", 2011, 1300));
            motoRepository.save(new Moto("Kawasaki", 2013, 30, Times.createTimes(4)));
            vanRepository.save(new Van("Mercedes", 2010, 3000, CargoCapacity.createCargoCapacity(700)));

            for (var car : carRepository.findAll()) {
                log.info(car.toString());
            }
            Stream.of(motoRepository.findAll()).forEach(el -> log.info(el.toString()));
            //reference operator(System.out::println); ||
        };
    }
}
