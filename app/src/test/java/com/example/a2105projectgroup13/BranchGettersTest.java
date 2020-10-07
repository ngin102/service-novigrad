package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests methods from the Branch class.
 */
public class BranchGettersTest {
    @Test
    public void testGetters() {
        User userBob = new Branch("Supa", "Hackerman", "Branch Account");
        assertEquals("Supa", userBob.getFirstName());
        assertEquals("Hackerman", userBob.getLastName());
        assertEquals("Branch Account", userBob.getAccountType());
    }
}