package com.mic.garage.exception.advice;

import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class VehicleArgsNotAcceptedAdvice {
    @ResponseBody
    @ExceptionHandler(VehicleArgsNotAcceptedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String vehicleArgsNotAcceptedHandler(VehicleArgsNotAcceptedException ex) {
        return ex.getMessage();
    }
}
