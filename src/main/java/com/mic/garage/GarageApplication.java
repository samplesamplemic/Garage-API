package com.mic.garage;

import com.mic.garage.entity.*;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.repository.VanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//It's important to mention that the latest version of Swagger specification, now known as OpenAPI 3.0,
// is better supported by the Springdoc project and should be used for documenting Spring REST API.
@SpringBootApplication
public class GarageApplication {

    public static void main(String[] args) {
        SpringApplication.run(GarageApplication.class, args);
    }
}
