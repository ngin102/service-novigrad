package com.example.a2105projectgroup13;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the Branch class.
 * It can be used to create instances of type Branch, a subclass of User.
 */
public class Branch extends User{
    private String address;
    private String phoneNumber;
    private List<Service> services;
    private List<WorkingHours> workingHours;

    public Branch(){
    }

    public Branch(String firstName, String lastName, String accountType) {
        super(firstName, lastName, accountType);
        this.services = new ArrayList<Service>();
        this.workingHours = new ArrayList<WorkingHours>();
        this.address = "Not set";
        this.phoneNumber = "Not set";
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getAddress(){return address;}

    public String getPhoneNumber(){return phoneNumber;}

    public void addToWorkingHours(WorkingHours hours){
        workingHours.add(hours);
    }
}
