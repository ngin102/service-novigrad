package com.example.a2105projectgroup13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests methods from the Review class.
 */
public class ReviewGettersTest {
    @Test
    public void testGetters() {
        Branch branch = new Branch();
        Customer customer = new Customer();
        Review testReview = new Review("Amazing!", 5, branch, customer);

        assertEquals("Amazing!", testReview.getComment());
        assertEquals(5, testReview.getRating());
        assertEquals(branch, testReview.getBranchReviewed());
        assertEquals(customer, testReview.getReviewer());
    }
}
