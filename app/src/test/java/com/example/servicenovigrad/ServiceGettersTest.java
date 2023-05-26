package com.example.servicenovigrad;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ServiceGettersTest {

    @Test
    public void testGetters() {
        Service testService = new Service("a service", "20.00");

        ArrayList<Requirement> requirementList = new ArrayList<Requirement>();

        assertEquals("a service", testService.getName());
        assertEquals("20.00", testService.getPrice());
        assertEquals(requirementList, testService.getRequirements());
    }

}