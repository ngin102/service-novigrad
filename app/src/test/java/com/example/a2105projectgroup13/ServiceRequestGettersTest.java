package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests methods from the ServiceRequest class.
 */
public class ServiceRequestGettersTest {
    @Test
    public void testGetters(){
        ServiceRequest testServiceRequest = new ServiceRequest("Request Number 1", "Accepted", "1234", "Driver's License", "5678");

        assertEquals("Request Number 1", testServiceRequest.getRequestNumber());
        assertEquals("Accepted", testServiceRequest.getStatus());
        assertEquals("1234", testServiceRequest.getBranchUid());
        assertEquals("Driver's License", testServiceRequest.getServiceName());
        assertEquals("5678", testServiceRequest.getCustomerUid());
    }
}
