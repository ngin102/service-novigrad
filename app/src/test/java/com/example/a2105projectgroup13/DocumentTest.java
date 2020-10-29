package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testGetters() {
        Form testDocument = new Form("document", "Photo ID");
        assertEquals("document", testDocument.getType());
        assertEquals("Photo ID", testDocument.getName());
    }

}