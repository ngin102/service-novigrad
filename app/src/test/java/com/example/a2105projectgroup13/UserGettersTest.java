package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests methods from the User class.
 */
public class UserGettersTest {
    @Test
    public void testGetter1() {
        User userTest = new User("Yennefer", "Vengerberg", "Customer Account");
        assertEquals("Yennefer", userTest.getFirstName());
        assertEquals("Vengerberg", userTest.getLastName());
        assertEquals("Customer Account", userTest.getAccountType());
    }
    @Test
    public void testGetter2() {
        User userTest = new User("Sigismund", "Dijkstra", "Branch Account");
        assertEquals("Sigismund", userTest.getFirstName());
        assertEquals("Dijkstra", userTest.getLastName());
        assertEquals("Branch Account", userTest.getAccountType());
    }
}
