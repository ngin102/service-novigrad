package com.example.servicenovigrad;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequirementGettersTest {

    @Test
    public void testGetters() {
        Requirement testRequirement1 = new Requirement("form", "Applicant Form");
        Service service = new Service("Service", "5.00");
        testRequirement1.setService(service);
        assertEquals("form", testRequirement1.getType());
        assertEquals("Applicant Form", testRequirement1.getName());
        assertEquals(service, testRequirement1.getService());
    }
}