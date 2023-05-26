package com.example.servicenovigrad;

public class WorkingHours {
    //Instance variables.
    private String day;
    private String openTime;
    private String closeTime;

    private Branch branch;

    //Constructors
    public WorkingHours(){
    }

    public WorkingHours(Branch branch, String day, String openTime, String closeTime) {
        this.branch = branch;
        this.day = day;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    //Getter functions.
    public Branch getBranch(){
        return branch;
    }

    public String getDay(){
        return day;
    }

    public String getOpenTime(){
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }
}

