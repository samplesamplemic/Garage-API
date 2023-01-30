package com.mic.garage;


import com.mic.garage.controller.CarController;
import com.mic.garage.controller.MotoController;
import com.mic.garage.controller.VanController;
import com.mic.garage.entity.Car;
import com.mic.garage.entity.Doors;
import com.mic.garage.entity.Fuel;
import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import com.mic.garage.exception.VehicleNotFoundException;
import com.mic.garage.model.CarDto;
import com.mic.garage.repository.CarRepository;
import com.mic.garage.repository.MotoRepository;
import com.mic.garage.repository.VanRepository;
import com.mic.garage.service.CarServiceImpl;
import com.mic.garage.service.assembler.CarModelAssembler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GarageApplicationUnitTest {

    Car car;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarModelAssembler carModelAssembler;
    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    void setup() {
        //   carService = new CarServiceImpl(carRepository, carModelAssembler);
        car = new Car("Fiat", 2011, 1200, Doors.createDoors(3), Fuel.DIESEL);
    }

    @Test
    void FindById_return_oneCar() {
        Long id = 1L;
        given(carRepository.findById(anyLong())).willReturn(Optional.of(car));
        EntityModel<CarDto> carDto = carService.readById(id);
        assertThat(carDto.getContent().getVehicleYear()).isEqualTo(2011);
        verify(carRepository, times(1)).findById(id);
    }

    @Test
    void FindById_return_emptyData_should_thrown_VehicleNotFoundEx() {
        Long id = 1L;
        when(carRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VehicleNotFoundException.class, () -> carService.readById(id));
    }

    @Test
    void FindAll_return_allCar() {
        when(carRepository.findAll()).thenReturn(List.of(car, car));
        assertThat(carService.readAll()).hasSize(2);
        assertEquals(2, carService.readAll().getContent().size());
    }

    @Test
    void Update_should_update_OneCar_byId() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        //car.setVehicleYear(2020);
        CarDto carDto = CarDto.builder()
                .id(1L)
                .brand(car.getBrand())
                .engineCapacity(car.getEngineCapacity())
                .vehicleYear(2020)
                .fuel(car.getFuel())
                .doors(Doors.createDoors(car.getDoors()))
                .build();
        carService.update(carDto, 1L);
        assertEquals(2020, carService.readById(1L).getContent().getVehicleYear());
    }

    @Test
    void Delete_should_delete_onCar_byId() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        doNothing().when(carRepository).deleteById(anyLong());

        //assertThat(carRepository.findById(1L).isPresent());
        carService.delete(1l);
        assertThat(carRepository.findById(1L).isEmpty());

        verify(carRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(carRepository);
    }

    @Test
    void Validation_doorsClass_return_ArgsNotAcceptEx() {
        assertThrows(VehicleArgsNotAcceptedException.class, () -> Doors.createDoors(1));
    }

//    @Test
//    void getAllCars() {
//        carService.readAll();
//        verify(carRepository).findAll();
//    }
}


