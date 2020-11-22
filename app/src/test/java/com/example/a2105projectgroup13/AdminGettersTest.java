package com.example.a2105projectgroup13;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AdminGettersTest {

    @Test
    public void validateAdmin() {
        Admin newAdmin = new Admin("Admin", "Admin", "Admin");

        // add a service, document, and form to the service list
        Service newService = newAdmin.createService("Photo ID", "100.00");
        newAdmin.addToServices(newService);
        Document newDocument = newAdmin.createDocument("Document", "Document Name", "PDF", "A test document.");
        Form newForm = newAdmin.createForm("Form", "Name");

        // verify the contents of the new service, form, and document
        assertEquals("100.00", newService.getPrice());
        assertEquals("Photo ID", newService.getName());

        assertEquals("Document", newDocument.getType());
        assertEquals("Document Name", newDocument.getName());
        assertEquals("PDF", newDocument.getFileType());
        assertEquals("A test document.", newDocument.getDescription());

        assertEquals("Form", newForm.getType());
        assertEquals("Name", newForm.getName());

        // access the service list in admin
        List<Service> serviceList = newAdmin.getServices();

        // verify the service list of the admin contains the service we stored
        assertEquals(newService, serviceList.get(0));

    }

}