package com.example.servicenovigrad;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorkingHoursGettersTest {

    @Test
    public void testGetters() {
        Branch branch = new Branch("Jim", "Jones", "Branch Account");
        WorkingHours testWorkingHours = new WorkingHours(branch, "Saturday", "03:00", "11:00");

        assertEquals(branch, testWorkingHours.getBranch());
        assertEquals("Saturday", testWorkingHours.getDay());
        assertEquals("03:00", testWorkingHours.getOpenTime());
        assertEquals("11:00", testWorkingHours.getCloseTime());
    }

}