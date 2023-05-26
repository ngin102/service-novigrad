package com.example.servicenovigrad;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests methods from the Branch class.
 */
public class BranchGettersTest {
    @Test
    public void testGetters() {
        Branch userBob = new Branch("Supa", "Hackerman", "Branch Account");
        assertEquals("Supa", userBob.getFirstName());
        assertEquals("Hackerman", userBob.getLastName());
        assertEquals("Branch Account", userBob.getAccountType());

        userBob.setAddress("123 Sesame Street");
        assertEquals("123 Sesame Street", userBob.getAddress());
        userBob.setPhoneNumber("123-456-7890");
        assertEquals("123-456-7890", userBob.getPhoneNumber());
    }
}