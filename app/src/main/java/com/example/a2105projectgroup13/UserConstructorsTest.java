package com.example.a2105projectgroup13;

public class UserConstructorsTest {

    public static void main(String[] args) {
        User newUser = new User("B4iY9wXmTrQ0WtllxfCXUwvxEs12");
        // create a known user using User
//        UserConstructorsTest.createUserFromID("B4iY9wXmTrQ0WtllxfCXUwvxEs12");

        // create a known user using Customer
    }

    public static void createUserFromID(String userID) {
        User newUser = new User(userID);
//        System.out.println(newUser.getFirstName() + " " + newUser.getLastName() + " has a " + newUser.getAccountType());
    }
}
