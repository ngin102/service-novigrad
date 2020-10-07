package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BranchGettersTest {
    @Test
    public void gettersWork_forRegularConstructor() {
        User userBob = new Branch("Test", "Testing", "Customer Account");
        assertEquals("Bob", userBob.getFirstName());
        assertEquals("Winter", userBob.getLastName());
        assertEquals("Branch Account", userBob.getAccountType());
    }

    @Test
    public void gettersWork_forIDConstructor() {
        User userBob = new Branch("6tepDzhicXeMec2OZg35vuURa5T2");
        assertEquals("Bob", userBob.getFirstName());
        assertEquals("Winter", userBob.getLastName());
        assertEquals("Branch Account", userBob.getAccountType());
    }
}