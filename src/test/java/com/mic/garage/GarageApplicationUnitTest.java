package com.mic.garage;


import com.mic.garage.exception.VehicleArgsNotAcceptedException;
import com.mic.garage.exception.VehicleNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GarageApplicationUnitTest {


    @Test
    public void testVehicleNotFoundException() {
        Long id = 2l;
        RuntimeException ex = new VehicleNotFoundException(id);
        String message= "Could not find vehicle "+id;
        //RuntimeException exCompared = new RuntimeException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testVehicleArgsNotAcceptedException(){
        RuntimeException ex = new VehicleArgsNotAcceptedException("work?");
        String msg = "work?";
        String msgFail = "not work";
        assertEquals(msg, ex.getMessage());
        assertTrue(msg == ex.getMessage());
        assertFalse(msgFail== ex.getMessage());
    }

    //test toString method
    //get and set method?
    //self-validation in VO?
}


