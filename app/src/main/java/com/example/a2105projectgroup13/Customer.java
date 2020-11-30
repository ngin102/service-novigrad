package com.example.a2105projectgroup13;

/**
 * This class is the Customer class.
 * It can be used to create instances of type Customer, a subclass of User.
 */
public class Customer extends User{
    public Customer(){
    }

    public Customer(String firstName, String lastName, String accountType) {
        super(firstName, lastName, accountType);
    }
}