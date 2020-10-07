package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserGettersTest {
    @Test
    public void testGetters() {
        User userTest = new User("Test", "Testing", "Customer Account");
        assertEquals("Test", userTest.getFirstName());
        assertEquals("Testing", userTest.getLastName());
        assertEquals("Customer Account", userTest.getAccountType());
    }
}
