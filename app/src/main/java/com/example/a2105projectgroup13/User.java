package com.example.a2105projectgroup13;

public class User{

    private String firstName;
    private String lastName;
    private String accountType;
    private int userID;

    public User(String nameNumOne, String nameNumTwo, String acType, int userID){
        this.firstName = nameNumOne;
        this.lastName = nameNumTwo;
        this.accountType = acType;
        this.userID = userID;
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

    public int getUserID() {
        return userID;
    }
}