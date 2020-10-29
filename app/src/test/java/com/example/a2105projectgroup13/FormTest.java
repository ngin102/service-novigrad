package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.*;

public class FormTest {

    @Test
    public void testGetters() {
        Form testForm = new Form("form", "Address");
        assertEquals("form", testForm.getType());
        assertEquals("Address", testForm.getName());
    }

}