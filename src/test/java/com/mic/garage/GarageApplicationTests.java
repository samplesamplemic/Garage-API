package com.mic.garage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mic.garage.entity.Car;
import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Fuel;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.CarDto;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.service.CarServiceImpl;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.text.MessageFormat;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //run spring context
@AutoConfigureMockMvc //bean of MockMvc injected by @SpringBootTest || mock the applicationContext
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

    //TestRestTemplate testRestTemplate = new TestRestTemplate();

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
        assertThat(Objects.requireNonNull(carMatch.getContent()).getId()).isGreaterThan(0);
        assertEquals("Fiat", carMatch.getContent().getBrand());
    }

    @Test
    void CreateCar_should_mock_postReq_andSave_OneCar() throws Exception {
        ResultActions result = mockMvc.perform(post(urlCar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand", is("Fiat")))
                .andDo(print());
    }

    @Test
    void CreateCar_should_mock_POSTReq_return_ArgsNotAcceptedEx() throws Exception {
        MvcResult result = mockMvc.perform(post(urlCar)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                  "id": null,
                                  "brand": "Fiat",
                                  "vehicleYear": 2011,
                                  "engineCapacity": 1000,
                                  "doors": 1,
                                 }
                                """))
                .andExpect(status().isNotAcceptable())
                .andDo(print())
                .andReturn();

        String getRes = result.getResponse().getContentAsString();
        assertThat(getRes == new VehicleArgsNotAcceptedException("The value of doors must be between 3 or 5.").getMessage());
    }

//    @Test
//    void aaa() {
//        HttpHeaders reqHeaders = new HttpHeaders();
//        reqHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
//
//        HttpEntity<String> reqEntity = new HttpEntity<>("""
//                 {
//                  "id": null,
//                  "brand": "Fiat",
//                  "vehicleYear": 2011,
//                  "engineCapacity": 1000,
//                  "doors": 3,
//                 }
//                """, reqHeaders);
//
//        ResponseEntity<Void> response = this.testRestTemplate
//                .exchange(urlCar, HttpMethod.POST, reqEntity, Void.class);
//
//        assertThat(response.getStatusCode())
//                .isEqualTo(201);
//    }

    @Test
    void GetAllCars_should_findAndReturn_allCars() {
        CollectionModel<EntityModel<CarDto>> carsMatch = carService.readAll();
        assertThat(carsMatch).isNotNull();
        assertThat(carsMatch.getContent().size()).isEqualTo(2);
        assertEquals("Fiat", carsMatch.getContent().stream().toList().get(1).getContent().getBrand());
    }

    @Test
    void GetAllCars_should_mock_GETReq_return_allCars() throws Exception {
        ResultActions result = mockMvc.perform(get(urlCar))
                .andExpect(status().isOk())
                //index 1 in array because there already is one car saved by data.sql file;
                .andExpect(jsonPath(MessageFormat.format("$._embedded.cars[{0}].brand", 1), is("Fiat")))
                .andExpect(jsonPath(MessageFormat.format("$._embedded.cars[{0}].vehicleYear", 1), is(2011)))
                .andExpect(jsonPath(MessageFormat.format("$._embedded.cars[{0}].engineCapacity", 1), is(1200)))
                .andDo(print());

    }

    @Test
    void GetCarById_should_findById_AndReturn_oneCar() {
        EntityModel<CarDto> carMatch = carService.readById(id);
        assertThat(carMatch).isNotNull();
        assertThat(carMatch.getContent().getBrand()).isEqualTo("Fiat");
    }

    @Test
    void GetOneCar_should_mock_GETReqById_return_OneCar() throws Exception {
        ResultActions result = mockMvc.perform(get(urlCar.concat("/" + id)))
                .andExpect(jsonPath("$.brand", is("Fiat")))
                .andExpect(jsonPath("$.brand", is("Fiat")))
                .andExpect(jsonPath("$.brand", is("Fiat")))
                .andDo(print());
    }

    @Test
    void GetOneCar_should_mock_getReqById_return_VehicleNotFoundEx() throws Exception {
        MvcResult result = mockMvc.perform(get(urlCar.concat("/" + 400)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        String getRes = result.getResponse().getContentAsString();
        assertThat(getRes == new VehicleNotFoundException(400l).getMessage());
    }

    @Test
    void UpdateCar_should_findById_AndUpdate_searchedCar() {
        carDto.setEngineCapacity(1600);
        carDto.setVehicleYear(2015);
        carService.update(carDto, id);
        assertThat(carRepository.findById(carDto.getId()).get().getVehicleYear()).isEqualTo(2015);
    }

    @Test
    void UpdateCar_should_mock_PUTReq_return_updatedCar() throws Exception {
        carDto.setBrand("Ford");
        ResultActions result = mockMvc.perform(put(urlCar.concat("/" + id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", is("Ford")))
                .andDo(print());
    }

    @Test
    void DeleteCar_should_DeleteById_oneCar() {
        carService.delete(carDto.getId());
        assertThat(carRepository.findById(carDto.getId())).isEmpty();
    }

    @Test
    void DeleteCar_should_mock_DELETEReq_return_statusCodeAccepted() throws Exception {
        ResultActions result = mockMvc.perform(delete(urlCar.concat("/" + id)))
                .andExpect(status().isAccepted())
                .andDo(print());
    }
}


