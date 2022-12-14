package com.mic.garage.bootloader;

import com.mic.garage.model.Moto;
import com.mic.garage.model.Times;
import com.mic.garage.model.Vehicle;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(VehicleRepository repository, MotoRepository motoRepository) {
        return args -> {

            motoRepository.save(new Moto("Kawasaki", 2013, 30, Times.createTimes(4)));
            //repository.save(new Vehicle("Ford", 2006, 1300));
            //repository.save(new Vehicle("Kawasaki", 2013, 300));
//            for (var v : repository.findAll()) {
//                log.info(v.toString());
//            }
            for (var moto : motoRepository.findAll()) {
                log.info(moto.toString());
            }
        };
    }
}
