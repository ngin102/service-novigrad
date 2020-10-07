package com.example.a2105projectgroup13;

/**
 * This class is the Customer class.
 * It can be used to create instances of type Customer, a subclass of User.
 *
 * Please note: Admin, Customer and Branch all have unique operations that set them apart from one another
 * and from their parent class, User. However, these operations are not implemented in this Deliverable,
 * but they will be implemented in future Deliverables.
 */
public class Customer extends User{
    public Customer(String firstName, String lastName, String accountType) {
        super(firstName, lastName, accountType);
    }
}