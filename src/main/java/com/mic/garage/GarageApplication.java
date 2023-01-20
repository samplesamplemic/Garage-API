package com.mic.garage;

import com.mic.garage.entity.*;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.repository.VanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//It's important to mention that the latest version of Swagger specification, now known as OpenAPI 3.0,
// is better supported by the Springdoc project and should be used for documenting Spring REST API.
@SpringBootApplication
public class GarageApplication implements CommandLineRunner {

    private final MotoRepository motoRepository;
    private final VanRepository vanRepository;

    //injection dependency by constructor;
    public GarageApplication(MotoRepository motoRepository, VanRepository vanRepository) {
        this.motoRepository = motoRepository;
        this.vanRepository = vanRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(GarageApplication.class, args);
    }

    //By implementing the CommandLineRunner, the run() method will be executed after the application starts.
    @Override
    public void run(String... args) throws Exception {
        motoRepository.save(new Moto("Kawasaki", 2013, 30, Times.createTimes(4)));
        vanRepository.save(new Van("Mercedes", 2010, 3000, CargoCapacity.createCargoCapacity(700)));
    }
}
