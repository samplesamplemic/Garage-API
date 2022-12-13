package com.mic.garage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class VehicleNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(VehicleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String vehicleNotFoundHandler(VehicleNotFoundException ex){
        return ex.getMessage();
    }
}
