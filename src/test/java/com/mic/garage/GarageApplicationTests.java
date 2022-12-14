package com.mic.garage;

import com.mic.garage.exception.VehicleNotFoundException;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@SpringBootTest
class GarageApplicationTests {

    private HttpResponse httpResponse;
    private String url;

    @BeforeEach
    void setUp() {
    }

    @Test                                      //an error of type i/o;
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
        Long id = Long.valueOf(3);
        url = "http://localhost:8080/garage/moto/" + id;
        httpResponse = HttpClientBuilder.create().build().execute(new HttpGet(url));
        HttpEntity entity = httpResponse.getEntity();
        String resBody = EntityUtils.toString(entity, "UTF-8");
        Exception ex = new VehicleNotFoundException(id);
        Assertions.assertEquals(ex.getMessage(), resBody);

    }
}