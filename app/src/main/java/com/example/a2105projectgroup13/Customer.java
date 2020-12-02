package com.example.a2105projectgroup13;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the Customer class.
 * It can be used to create instances of type Customer, a subclass of User.
 */
public class Customer extends User{
    private List<Review> reviews;

    public Customer(){
    }

    public Customer(String firstName, String lastName, String accountType) {
        super(firstName, lastName, accountType);
        this.reviews = new ArrayList<Review>();
    }

    public Review createReview(String comment, int rating, Branch branchReviewed){
        Customer thisCustomer = new Customer(getFirstName(), getLastName(), getAccountType());
        Review writtenReview = new Review(comment, rating, branchReviewed, thisCustomer);
        return writtenReview;
    }
}