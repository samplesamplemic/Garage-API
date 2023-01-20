package com.mic.garage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.entity.Car;
import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Fuel;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.service.CarServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

@SpringBootTest //run spring context
@AutoConfigureMockMvc //bean of MockMvc injected by @SpringBootTes
class GarageApplicationTests {

    private HttpResponse httpResponse;
    private String url;

    @Autowired //dependency injection by setter || can be substituted by a constructor;
    // if autowired not used, there is an error: java.lang.NullPointerException:
    // Cannot invoke "com.mic.garage.repository.CarRepository.save(Object)" because "this.carRepository" is null
    private CarRepository carRepository; //final

    @Autowired
    private MockMvc mockMvc;

    //@Mock specifies the fields in which Mockito should inject mock objects;
    //@MockBean annotation to add mock objects to the Spring application context.
    //Replace a bean that already exists in the application context with a mock
    @MockBean
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
    }

    //1) controller/business layer
    //Status code
    @Test
    void testStatusCodeFound() throws IOException {
        url = "http://localhost:8080/garage/moto";
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url));
        Assertions.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }

    @Test
    void testStatusCodeNotFound() throws IOException { //an error of type i/o;
        //HttpUriRequest: Extended version of the HttpRequest interface that
        //provides convenience methods to access request properties such as request URI and method type.
        String id = "3";
        url = "http://localhost:8080/garage/moto/" + id;
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url));
        Assertions.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void testStatusCodeNotFoundDeleteCar() throws Exception {
        mockMvc.perform(delete("http://localhost:8080/garage/moto/2"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testStatusCodeFoundDeleteCar() throws Exception {
        carRepository.save(new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL));
        mockMvc.perform(delete("http://localhost:8080/garage/cars/1"))
                .andExpect(status().isAccepted())
                .andDo(print());
    }

    @Test
    void testResponseBodyNotFoundExceptionAdvice() throws IOException {
        Long id = Long.valueOf(30);
        url = "http://localhost:8080/garage/moto/" + id;
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url));
        HttpEntity entity = httpResponse.getEntity();
        String resBody = EntityUtils.toString(entity, "UTF-8");
        Exception ex = new VehicleNotFoundException(id);
        Assertions.assertEquals(ex.getMessage(), resBody);
    }

    //1) repository/data & service layer

    @Test
    void testCarRepositorySave() {
        Car car = carRepository.save(new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL));
        assertThat(car).hasFieldOrPropertyWithValue("brand", "Alfa Romeo");
    }

    @Test
    void testDeleteCar() throws Exception {
        Car car = carRepository.save(new Car("Alfa Romeo", 2011, 1300, Doors.createDoors(3), Fuel.DIESEL));
        carRepository.deleteById(car.getId());
        boolean deleteCar = carRepository.findById(car.getId()).isPresent();
        assertThat(deleteCar).isFalse();
    }
}

