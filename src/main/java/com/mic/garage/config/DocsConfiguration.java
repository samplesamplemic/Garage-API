package com.mic.garage.config;

import com.mic.garage.model.CarDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@OpenAPIDefinition
public class DocsConfiguration {

    List<Integer> doors = List.of(3,5);

    @Bean
    public OpenAPI customSchemaCar(){
       return new OpenAPI()
                .components(new Components()
                        .addSchemas("Car", new Schema()
                                .addProperty("brand", new StringSchema().example("string"))
                                .addProperty("vehicleYear", new IntegerSchema().example(0))
                                .addProperty("engineCapacity", new IntegerSchema().example(0))
                                .addProperty("doors", new ArraySchema().example(doors))
                                .addProperty("fuel", new StringSchema().example("Diesel/Petrol"))
                                ));
    }
}
