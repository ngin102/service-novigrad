package com.example.a2105projectgroup13;

public class User{

    private String firstName;
    private String lastName;
    private String accountType;

    public User(String firstName, String lastName, String accountType){
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
    }

    public User(String userID) {
        // empty for now
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getAccountType(){
        return accountType;
    }
}