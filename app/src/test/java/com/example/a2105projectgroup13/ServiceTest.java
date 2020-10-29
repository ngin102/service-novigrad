package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServiceTest {

    @Test
    public void testGetters() {
        Service testService = new Service("Request Loan", "19.99");
        assertEquals("Request Loan", testService.getName());
        assertEquals(19.99, testService.getPrice(), 0);
    }
}