package com.mic.garage;

import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.Car;
import com.mic.garage.model.Doors;
import com.mic.garage.model.Fuel;
import com.mic.garage.repository.CarRepository;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@SpringBootTest
class GarageApplicationTests {

    private HttpResponse httpResponse;
    private String url;

      @Autowired //? automatically connection with bean
    //if autowired not used, there is an error: java.lang.NullPointerException:
    // Cannot invoke "com.mic.garage.repository.CarRepository.save(Object)" because "this.carRepository" is null
    CarRepository carRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testStatusCodeFound() throws IOException {
        url = "http://localhost:8080/garage/moto";
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url));
        Assertions.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }

    @Test
        //an error of type i/o;
    void testStatusCodeNotFound() throws IOException {
        //HttpUriRequest: Extended version of the HttpRequest interface that
        //provides convenience methods to access request properties such as request URI and method type.
        //HttpUriRequest request1 = new HttpGet(url)
        String id = "3";
        url = "http://localhost:8080/garage/moto/" + id;
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url));
        Assertions.assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void testResponseBodyNotFoundException() throws IOException {
        Long id = Long.valueOf(30);
        url = "http://localhost:8080/garage/moto/" + id;
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url));
        HttpEntity entity = httpResponse.getEntity();
        String resBody = EntityUtils.toString(entity, "UTF-8");
        Exception ex = new VehicleNotFoundException(id);
        Assertions.assertEquals(ex.getMessage(), resBody);
    }

    @Test
    void testCarRepositorySave() {
        Car car = carRepository.save(new Car(Doors.createDoors(3), Fuel.DIESEL, "Alfa Romeo", 2011, 1300));
        assertThat(car).hasFieldOrPropertyWithValue("brand", "Alfa Romeo");
    }
}