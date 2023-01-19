package com.mic.garage.config;

import com.mic.garage.model.CarDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@OpenAPIDefinition
public class DocsConfiguration {

    //private List<Integer> doors = List.of(3, 5);

    @Bean
    public OpenAPI customSchemaCar() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("Vehicle", new Schema()
                                .addProperty("brand", new StringSchema().example("string"))
                                .addProperty("vehicleYear", new IntegerSchema().example(0))
                                .addProperty("engineCapacity", new IntegerSchema().example(0))
                        )
                        .addSchemas("Car", new Schema()
                                .addAllOfItem(new Schema().$ref("#/components/schemas/Vehicle"))
                                .addProperty("doors", new IntegerSchema().example(0).description("The number must be 3 or 5"))
                                .addProperty("fuel", new StringSchema().example("Diesel/Petrol").description("The fuel must be diesel or petrol"))
                        )
                        .addSchemas("embedded", new Schema()
                                .addProperty("embedded", new ObjectSchema()
                                        .addProperty("cars", new ArraySchema().items(new ObjectSchema()
                                                .addAllOfItem(new Schema().$ref("#/components/schemas/Car"))
                                                .addProperty("_links", new ObjectSchema()
                                                        .addProperty("self", new ObjectSchema()
                                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/cars/id")))
                                                        .addProperty("cars", new ObjectSchema()
                                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/cars")))))
                                        ))
                                .addProperty("_links", new ObjectSchema()
                                        .addProperty("self", new ObjectSchema()
                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/cars"))))));
    }
}
