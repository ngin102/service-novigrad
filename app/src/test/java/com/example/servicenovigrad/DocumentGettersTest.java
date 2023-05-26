package com.example.servicenovigrad;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentGettersTest {

    @Test
    public void testGetters() {
        Document testDocument = new Document("document", "Photo ID", "PDF", "This is a document");
        Service service2 = new Service("a service", "20.00");
        testDocument.setService(service2);

        assertEquals("document", testDocument.getType());
        assertEquals("Photo ID", testDocument.getName());
        assertEquals("PDF", testDocument.getFileType());
        assertEquals("This is a document", testDocument.getDescription());
        assertEquals(service2, testDocument.getService());
    }

}