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
class GarageApplicationTests {

    private HttpResponse httpResponse;
    String urlCar = "http://localhost:8080/garage/cars";
    Car car;
    CarDto carDto;
    CarModelAssembler carModelAssembler;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired //@MockBean
    CarRepository carRepository;

    @MockBean
    CarServiceImpl carService;

    @BeforeEach
    void setup() {
        carModelAssembler = new CarModelAssembler();
        car = new Car("Fiat", 2011, 1200, Doors.createDoors(3), Fuel.DIESEL);
        //carRepository.save(car);
        carDto = CarDto.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .engineCapacity(car.getEngineCapacity())
                .vehicleYear(car.getVehicleYear())
                .fuel(car.getFuel())
                .doors(Doors.createDoors(car.getDoors()))
                .build();
    }

    //Integration test
    //1) web & service layer
    @Test
    void testStatusCode_Ok_MatchObjRepo_GetCars() throws Exception {
        Stream<CarDto> carsStream = carRepository.findAll().stream()
                .map(car -> new CarDto(car.getId(), car.getBrand(), car.getVehicleYear(), car.getEngineCapacity(), Doors.createDoors(car.getDoors()), car.getFuel()));
        List<EntityModel<CarDto>> carsDto = carsStream.map(carModelAssembler::toModel)
                .collect(Collectors.toList());

        when(carService.readAll()).thenReturn(CollectionModel.of(carsDto));
        ResultActions result = mockMvc.perform(get(urlCar, accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.cars[0].brand", is("Alfa Romeo")))
                .andExpect(jsonPath("_embedded.cars[0].vehicleYear", is(2011)))
                .andExpect(jsonPath("_embedded.cars[0].engineCapacity", is(1300)))
                .andExpect(jsonPath("_embedded.cars[0].doors", is(3)))
                .andExpect(jsonPath("_embedded.cars[0].fuel", is("Diesel")))
                .andDo(print());
    }

    @Test
    void testStatusCode_Ok_MatchObjRepo_GetOneCar() throws Exception {
        carRepository.save(car);
        carDto.setId(car.getId());
        Long id = carDto.getId();
        carModelAssembler.toModel(carDto);
        when(carService.readById(id)).thenReturn(EntityModel.of(carDto));
        mockMvc.perform(get(urlCar + "/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", is("Fiat")))
                .andDo(print());
    }

    @Test
    void testStatusCode_NotFound_GetOneCar() throws Exception { //is this a unit or an integration test? || unit?
        Long id = 3L; //by mocking the service, doesn't throw error by itself
        given(carService.readById(id)).willThrow(new VehicleNotFoundException(id));
        ResultActions result = mockMvc.perform(get(urlCar + "/" + id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    void testStatusCode_Created__CreateCar() throws Exception {
        when(carService.create(carDto)).thenReturn(carDto);
        ResultActions result = mockMvc.perform(post(urlCar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand", is("Fiat")))
                .andDo(print());
    }

    @Test
    void testStatusCode_Ok_MatchObjRepo_UpdateCar() throws Exception {
        carRepository.save(car);
        carDto.setId(car.getId());
        carDto.setBrand("BMW");
        carDto.setVehicleYear(2006);
        when(carService.update(carDto, carDto.getId())).thenReturn(carDto);
        ResultActions result = mockMvc.perform(put(urlCar + "/" + carDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", is("BMW")))
                .andExpect(jsonPath("$.vehicleYear", is(2006)))
                .andDo(print());
    }

    @Test
    void testStatusCode_Accepted_DeleteCar() throws Exception {
        Long id = 2L;
        carModelAssembler.toModel(carDto);
        when(carService.readById(id)).thenReturn(EntityModel.of(carDto));
        ResultActions result = mockMvc.perform(delete(urlCar + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andDo(print());
    }

    @Test
        //need application is working || can it be a test?
    void testResponseBody_Vehicle_NotFound_Advice() throws IOException {
        Long id = 30L;
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(urlCar + "/" + id));
        HttpEntity entity = httpResponse.getEntity();
        String resBody = EntityUtils.toString(entity, "UTF-8");
        System.out.println(resBody);
        Exception ex = new VehicleNotFoundException(id);
        assertEquals(ex.getMessage(), resBody);
    }

    @Test
    void testResponseBody_Vehicle_ArgsNotAccepted_Advice() throws Exception {
        try {
            MvcResult result = mockMvc.perform(post(urlCar).contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new CarDto(null, "Alfa Romeo", 2011, 1300, Doors.createDoors(1), Fuel.DIESEL)))
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn();
        } catch (RuntimeException result) {
            RuntimeException ex = new VehicleArgsNotAcceptedException("The value of doors must be between 3 or 5.");
            System.out.println(result);
            assertEquals(ex.getMessage(), result.getMessage());
        }
    }

}


