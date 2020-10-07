package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerGettersTest {
    @Test
    public void gettersWork_forRegularConstructor() {
        User userTest = new Customer("Test", "Testing", "Customer Account");
        assertEquals("Test", userTest.getFirstName());
        assertEquals("Test", userTest.getLastName());
        assertEquals("Customer Account", userTest.getAccountType());
    }

    @Test
    public void gettersWork_forIDConstructor() {
        User userTest = new Customer("6tepDzhicXeMec2OZg35vuURa5T2");
        assertEquals("Test", userTest.getFirstName());
        assertEquals("Test", userTest.getLastName());
        assertEquals("Customer Account", userTest.getAccountType());
    }
}