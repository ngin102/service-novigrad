package com.example.servicenovigrad;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests methods from the Customer class.
 */
public class CustomerGettersTest {
    @Test
    public void testGetters() {
        User userTest = new Customer("Geralt", "Rivia", "Customer Account");
        assertEquals("Geralt", userTest.getFirstName());
        assertEquals("Rivia", userTest.getLastName());
        assertEquals("Customer Account", userTest.getAccountType());
    }
}