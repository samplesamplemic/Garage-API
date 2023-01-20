package com.mic.garage;

import com.mic.garage.controller.CarController;
import com.mic.garage.controller.MotoController;
import com.mic.garage.entity.Car;
import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Fuel;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.repository.VanRepository;
import com.mic.garage.service.CarServiceImpl;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CarController.class)
public class GarageApplicationUnitTest {

    @MockBean
    CarRepository carRepository;

    @MockBean
    CarModelAssembler carModelAssembler;

    @MockBean
    CarServiceImpl carService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testStatusCodeNotFoundDeleteCar() throws Exception {
        Mockito.when(carRepository.findById(5L)).thenReturn(null);
        mockMvc.perform(delete("http://localhost:8080/garage/cars/2"))
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
}


