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
    @Autowired
    MockMvc mockMvc;

    //@MockBean
    @Autowired
    CarRepository carRepository;

    @MockBean
    CarServiceImpl carService;

    @BeforeEach
    void setup() {
        car = new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL);
        carRepository.save(car);
        CarDto.builder()
                .brand(car.getBrand())
                .build();
    }

    @Test
    void testStatusCodeOkGetCars() throws Exception {
        //with @Autowired on repository work, with mockBean not
        List<Car> cars = carRepository.findAll();
        for(Car car : cars){
            System.out.println(car);
        }
        CarModelAssembler carModelAssembler = new CarModelAssembler();
        Stream<CarDto> cars2 = carRepository.findAll().stream()
                .map(car -> new CarDto(car.getId(), car.getBrand(), car.getVehicleYear(), car.getEngineCapacity(), Doors.createDoors(car.getDoors()), car.getFuel()));
        List<EntityModel<CarDto>> carsDto = cars2.map(carModelAssembler::toModel)
                .collect(Collectors.toList());

        when(carService.readAll()).thenReturn(CollectionModel.of(carsDto));
        ResultActions result = mockMvc.perform(get(urlCar, accept(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.cars[0].brand", is("Alfa Romeo")))
                .andDo(print());
    }

    @Test
//is this a unit or an integration test? || unit?
    void testStatusCodeNotFoundGetOneCar() throws Exception {
        Long id = 3L; //by mocking the service, doesn't throw error by itself
        given(carService.readById(id)).willThrow(new VehicleNotFoundException(id));
        ResultActions result = mockMvc.perform(get(urlCar + "/" + id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
        //need application is working
    void testResponseBodyVehicleNotFoundAdvice() throws IOException {
        Long id = 30L;
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(urlCar + "/" + id));
        HttpEntity entity = httpResponse.getEntity();
        String resBody = EntityUtils.toString(entity, "UTF-8");
        System.out.println(resBody);
        Exception ex = new VehicleNotFoundException(id);
        assertEquals(ex.getMessage(), resBody);
    }

    @Test
    void testResBodyGetCars() {

    }

}

//    private HttpResponse httpResponse;
//    private String url;
//    private Car car;
//    private CarDto carDto;
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockBean
//    private CarRepository carRepository; //final
//    @MockBean
//    private CarServiceImpl carService;
//
//    @BeforeEach
//    void beforeEach() {
//        url = "http://localhost:8080/garage/cars";
//        car = new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL);
//        carDto = CarDto.builder()
//                .brand(car.getBrand())
//                .vehicleYear(car.getVehicleYear())
//                .engineCapacity(car.getEngineCapacity())
//                .doors(Doors.createDoors(car.getDoors()))
//                .fuel(car.getFuel())
//                .build();
//        carRepository.save(car);
//    }
//
//    //Integration test (need the application context to be loaded)
//    //1) controller/business layer
//    //Status code
//    @Test
//    void testStatusCodeFoundGetCars() throws Exception {
//        mockMvc.perform(get(url))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
//
//    @Test
//    void testStatusCodeNotFound() throws Exception { //an error of type i/o;
//        Long id = 3l;
//        when(carService.readById(id)).thenReturn(null);
//        mockMvc.perform(get(url + "/" + id))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    @Test
//    void testStatusCodeCreateCar() throws Exception {
//        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(carDto))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//    @Test
//    void testStatusCodeFoundDeleteCar() throws Exception {
//        Long id = 1l;
//        //Car car = new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL);
//        carRepository.save(car);
//        mockMvc.perform(delete(url + "/" + id))
//                .andExpect(status().isAccepted())
//                .andDo(print());
//    }
//
//    @Test
//    void testStatusCodeNotFoundDeleteCar() throws Exception {
//        Long id = 2l;
//        mockMvc.perform(delete(url + "/" + id))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    @Test
//    void testResponseBodyVehicleNotFoundAdvice() throws IOException {
//        Long id = Long.valueOf(30);
//        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url + "/" + id));
//        HttpEntity entity = httpResponse.getEntity();
//        String resBody = EntityUtils.toString(entity, "UTF-8");
//        Exception ex = new VehicleNotFoundException(id);
//        assertEquals(ex.getMessage(), resBody);
//    }
//
//    @Test
//    void testResponseBodyVehicleArgsNotAcceptedAdvice() throws Exception {
//        try {
//            MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(new CarDto(null, "Alfa Romeo", 2011, 1300, Doors.createDoors(1), Fuel.DIESEL)))
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andReturn();
//        } catch (RuntimeException result) {
//            RuntimeException ex = new VehicleArgsNotAcceptedException("The value of doors must be between 3 or 5.");
//            System.out.println(result);
//            assertEquals(ex.getMessage(), result.getMessage());
//        }
//    }
//
//    //2) persistence/service layer
//
//    @Test
//    void testCreateCar() throws Exception {
//
//        MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(carDto))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        //assertThat(carRepository.findById(4l).isPresent()); not work, return always true;
//    }

//}

