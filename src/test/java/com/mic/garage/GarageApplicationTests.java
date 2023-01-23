package com.mic.garage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mic.garage.entity.*;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.service.CarServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.*;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

@SpringBootTest //run spring context
@AutoConfigureMockMvc //bean of MockMvc injected by @SpringBootTes || mock the applicationContext
class GarageApplicationTests {

    private HttpResponse httpResponse;
    private String url;
    private Car car;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CarRepository carRepository; //final
    @MockBean
    private CarServiceImpl carService;

    @BeforeEach
    void beforeEach() {
        url = "http://localhost:8080/garage/cars";
    }

    //Integration test (need the application context to be loaded)
    //1) controller/business layer
    //Status code
    @Test
    void testStatusCodeFoundGetCars() throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testStatusCodeNotFound() throws IOException { //an error of type i/o;
        String id = "3";
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url + "/" + id));
        Assertions.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void testStatusCodeNotFoundDeleteCar() throws Exception {
        Long id = 2l;
        mockMvc.perform(delete(url + "/" + id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testStatusCodeFoundDeleteCar() throws Exception {
        Long id = 1l;
        Car car = new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL);
        carRepository.save(car);
        mockMvc.perform(delete(url + "/" + id))
                .andExpect(status().isAccepted())
                .andDo(print());
    }

    @Test
    void testResponseBodyVehicleNotFoundAdvice() throws IOException {
        Long id = Long.valueOf(30);
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url + "/" + id));
        HttpEntity entity = httpResponse.getEntity();
        String resBody = EntityUtils.toString(entity, "UTF-8");
        Exception ex = new VehicleNotFoundException(id);
        Assertions.assertEquals(ex.getMessage(), resBody);
    }

    @Test
    void testResponseBodyVehicleArgsNotAcceptedAdvice() throws Exception {

    }

    //2) persistence/service layer

    @Test
    void testCreateCar() throws Exception {
        Car car = new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL);
        objectMapper = new ObjectMapper();
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void testDeleteCar()  {
    }
}

