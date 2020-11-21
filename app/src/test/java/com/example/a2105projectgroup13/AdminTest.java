package com.example.a2105projectgroup13;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AdminTest {

    @Test
    public void validateAdmin() {
        Admin newAdmin = new Admin("Admin", "Admin", "Admin");

        // create a service in admin
        Service newService = newAdmin.createService("Photo ID", "100.00");
        newAdmin.addToServices(newService);

        // verify the contents of the new service
        assertEquals("100.00", newService.getPrice());
        assertEquals("Photo ID", newService.getName());

        // access the service list in admin
        List<Service> serviceList = newAdmin.getServices();

        // verify the service list of the admin contains the service we stored
        assertEquals(newService, serviceList.get(0));

    }

}