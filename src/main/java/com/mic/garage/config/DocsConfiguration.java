package com.mic.garage.config;

import com.mic.garage.model.CarDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Configuration of schema exposed by OpenAPI/Swagger
@Configuration
@OpenAPIDefinition
public class DocsConfiguration {

    @Bean
    public OpenAPI customSchemaCar() {
        return new OpenAPI()
                .components(new Components()

                        //general schema
                        .addSchemas("Vehicle", new Schema()
                                .addProperty("brand", new StringSchema().example("string"))
                                .addProperty("vehicleYear", new IntegerSchema().example(0))
                                .addProperty("engineCapacity", new IntegerSchema().example(0)))

                        .addSchemas("Links1", new Schema()
                                .addProperty("_links", new ObjectSchema()
                                        .addProperty("self", new ObjectSchema()
                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/vehicle/id")))
                                        .addProperty("vehicle", new ObjectSchema()
                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/vehicle")))))

                        //Car Schema
                        .addSchemas("Car", new Schema()
                                .addAllOfItem(new Schema().$ref("#/components/schemas/Vehicle"))
                                .addProperty("doors", new IntegerSchema().example(0).description("The number of doors must be 3 or 5"))
                                .addProperty("fuel", new StringSchema().example("Diesel/Petrol").description("The fuel must be diesel or petrol")))
                        .addSchemas("CarWithId", new Schema<>()
                                .addProperty("id", new IntegerSchema().example(0))
                                .addAllOfItem(new Schema<>().$ref("#/components/schemas/Car")))
                        .addSchemas("getAllCars", new Schema()
                                .addProperty("embedded", new ObjectSchema()
                                        .addProperty("cars", new ArraySchema().items(new ObjectSchema()
                                                .addAllOfItem(new Schema().$ref("#/components/schemas/CarWithId"))
                                                .addAllOfItem(new Schema().$ref("#/components/schemas/Links1")))
                                        ))
                                .addProperty("_links", new ObjectSchema()
                                        .addProperty("self", new ObjectSchema()
                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/cars")))))
                        .addSchemas("getOneCar", new Schema()
                                .addAllOfItem(new Schema().$ref("#/components/schemas/CarWithId"))
                                .addAllOfItem(new Schema().$ref("#/components/schemas/Links1")))


                        //moto schema
                        .addSchemas("moto", new Schema()
                                .addAllOfItem(new Schema().$ref("#/components/schemas/Vehicle"))
                                .addProperty("times", new IntegerSchema().example(0).description("The number of times must be 2 or 4"))
                        )
                        .addSchemas("motoWithId", new Schema<>()
                                .addProperty("id", new IntegerSchema().example(0))
                                .addAllOfItem(new Schema<>().$ref("#/components/schemas/moto")))
                        .addSchemas("getAllMoto", new Schema()
                                .addProperty("embedded", new ObjectSchema()
                                        .addProperty("moto", new ArraySchema().items(new ObjectSchema()
                                                .addAllOfItem(new Schema().$ref("#/components/schemas/motoWithId"))
                                                .addAllOfItem(new Schema().$ref("#/components/schemas/Links1")))
                                        ))
                                .addProperty("_links", new ObjectSchema()
                                        .addProperty("self", new ObjectSchema()
                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/moto")))))
                        .addSchemas("getOneMoto", new Schema()
                                .addAllOfItem(new Schema().$ref("#/components/schemas/motoWithId"))
                                .addAllOfItem(new Schema().$ref("#/components/schemas/Links1")))


                        //van schema
                        .addSchemas("van", new Schema()
                                .addAllOfItem(new Schema().$ref("#/components/schemas/Vehicle"))
                                .addProperty("cargoCapacity", new IntegerSchema().example(0).description("The number of cargo capacity must be positive"))
                        )
                        .addSchemas("vanWithId", new Schema<>()
                                .addProperty("id", new IntegerSchema().example(0))
                                .addAllOfItem(new Schema<>().$ref("#/components/schemas/van")))
                        .addSchemas("getAllVan", new Schema()
                                .addProperty("embedded", new ObjectSchema()
                                        .addProperty("van", new ArraySchema().items(new ObjectSchema()
                                                .addAllOfItem(new Schema().$ref("#/components/schemas/vanWithId"))
                                                .addAllOfItem(new Schema().$ref("#/components/schemas/Links1")))
                                        ))
                                .addProperty("_links", new ObjectSchema()
                                        .addProperty("self", new ObjectSchema()
                                                .addProperty("href:", new StringSchema().example("http://host:port/garage/van")))))
                        .addSchemas("getOneVan", new Schema()
                                .addAllOfItem(new Schema().$ref("#/components/schemas/vanWithId"))
                                .addAllOfItem(new Schema().$ref("#/components/schemas/Links1")))

                );
    }
}
