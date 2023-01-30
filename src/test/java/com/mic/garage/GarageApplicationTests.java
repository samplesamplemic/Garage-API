package com.mic.garage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mic.garage.controller.CarController;
import com.mic.garage.entity.*;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.CarDto;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.service.CarServiceImpl;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.accept;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest //run spring context
@AutoConfigureMockMvc //bean of MockMvc injected by @SpringBootTest || mock the applicationContext
@ActiveProfiles("test")
class GarageApplicationTests {

    String urlCar = "http://localhost:8080/garage/cars";
    Long id;
    Car car;
    CarDto carDto;
    CarModelAssembler carModelAssembler;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CarRepository carRepository;
    @Autowired
    CarServiceImpl carService;


    @BeforeEach
    void setUp() {
        carModelAssembler = new CarModelAssembler();
        car = new Car("Fiat", 2011, 1200, Doors.createDoors(3), Fuel.DIESEL);
        carRepository.save(car);
        carDto = CarDto.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .engineCapacity(car.getEngineCapacity())
                .vehicleYear(car.getVehicleYear())
                .fuel(car.getFuel())
                .doors(Doors.createDoors(car.getDoors()))
                .build();
        id = carDto.getId();
    }

    @AfterEach
    void cleanUpEach() {
        if (carRepository.findById(carDto.getId()).isPresent()) {
            carRepository.deleteById(carDto.getId());
        }
    }


    @Test
    void CreateCar_should_save_oneCar() {
        EntityModel<CarDto> carMatch = carService.readById(carDto.getId());
        assertThat(carMatch).isNotNull();
        assertThat(carMatch.getContent().getId()).isGreaterThan(0);
        assertEquals("Fiat", carMatch.getContent().getBrand());
    }

    @Test
    void MockCreateCar_should_mock_PostRequest_andSaved_OneCar() throws Exception {
        ResultActions result = mockMvc.perform(post(urlCar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand", is("Fiat")))
                .andDo(print());
    }

    @Test
    void GetAllCars_should_findAndReturn_allCars() {
        CollectionModel<EntityModel<CarDto>> carsMatch = carService.readAll();
        assertThat(carsMatch).isNotNull();
        assertThat(carsMatch.getContent().size()).isEqualTo(2);
        assertEquals("Fiat", carsMatch.getContent().stream().toList().get(1).getContent().getBrand());
    }

    @Test
    void GetCarById_should_findById_AndReturn_oneCar() {
        EntityModel<CarDto> carMatch = carService.readById(id);
        assertThat(carMatch).isNotNull();
        assertThat(carMatch.getContent().getBrand()).isEqualTo("Fiat");
    }

    @Test
    void UpdateCar_should_findById_AndUpdate_oneCar() {
        carDto.setEngineCapacity(1600);
        carDto.setVehicleYear(2015);
        carService.update(carDto, id);
        assertThat(carRepository.findById(carDto.getId()).get().getVehicleYear()).isEqualTo(2015);
    }

    @Test
    void DeleteCar_should_DeleteById_oneCar() {
        carService.delete(carDto.getId());
        assertThat(carRepository.findById(carDto.getId())).isEmpty();
    }
}


