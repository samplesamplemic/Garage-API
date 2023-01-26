package com.mic.garage;


import com.mic.garage.controller.CarController;
import com.mic.garage.controller.MotoController;
import com.mic.garage.controller.VanController;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.repository.VanRepository;
import com.mic.garage.service.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(CarController.class)
public class GarageApplicationUnitTest {

    String urlCar = "http://localhost:8080/garage/cars";

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private CarServiceImpl carService;

    @Test
    public void testVehicle_NotFound_Exception() {
        Long id = 2l;
        RuntimeException ex = new VehicleNotFoundException(id);
        String message= "Could not find vehicle "+id;
        //RuntimeException exCompared = new RuntimeException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testVehicle_ArgsNotAccepted_Exception(){
        RuntimeException ex = new VehicleArgsNotAcceptedException("work?");
        String msg = "work?";
        String msgFail = "not work";
        assertEquals(msg, ex.getMessage());
        assertTrue(msg == ex.getMessage());
        assertFalse(msgFail== ex.getMessage());
    }

    @Test
    void testStatusCode_NotFound_GetOneCar() throws Exception {
        Long id = 3L; //by mocking the service, doesn't throw error by itself
        given(carService.readById(id)).willThrow(new VehicleNotFoundException(id));
        ResultActions result = mockMvc.perform(get(urlCar + "/" + id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    //test toString method
    //get and set method?
    //self-validation in VO?
}


