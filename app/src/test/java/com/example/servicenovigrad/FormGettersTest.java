package com.example.servicenovigrad;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormGettersTest {

    @Test
    public void testGetters() {
        Form testForm = new Form("form", "Address");
        Service service1 = new Service("this is a service", "2.00");
        testForm.setService(service1);
        assertEquals("form", testForm.getType());
        assertEquals("Address", testForm.getName());
        assertEquals(service1, testForm.getService());
    }

}