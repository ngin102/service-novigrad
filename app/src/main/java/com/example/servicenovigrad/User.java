package com.example.servicenovigrad;

/**
 * This class is the User class.
 * It can be used to create instances of type "User."
 *
 * Please note: Admin, Customer and Branch all have unique operations that set them apart from one another
 * and from their parent class, User. However, these operations are not implemented in this Deliverable,
 * but they will be implemented in future Deliverables.
 */
public class User {

    private String firstName;
    private String lastName;
    private String accountType;

    // constructors

    public User(){
    }

    public User(String firstName, String lastName, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
    }

// public getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAccountType() {
        return accountType;
    }
}