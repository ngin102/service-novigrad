package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserGettersTest {
    @Test
    public void gettersWork_forRegularConstructor() {
        User userJohn = new User("Test", "Testing", "Customer Account");
        assertEquals("Test", userJohn.getFirstName());
        assertEquals("Testing", userJohn.getLastName());
        assertEquals("Customer Account", userJohn.getAccountType());
    }

    @Test
    public void gettersWork_forIDConstructor() {
        User userJohn = new User("6tepDzhicXeMec2OZg35vuURa5T2");
        assertEquals("Test", userJohn.getFirstName());
        assertEquals("Testing", userJohn.getLastName());
        assertEquals("Customer Account", userJohn.getAccountType());
    }
}
