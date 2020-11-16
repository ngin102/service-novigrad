package com.example.a2105projectgroup13;

import java.util.ArrayList;
import java.util.List;

public class WorkingHours {
    //Instance variables.
    private String day;
    private String hours;

    private Branch branch;

    //Constructors
    public WorkingHours(){
    }

    public WorkingHours(Branch branch, String day, String hours) {
        this.branch = branch;
        this.day = day;
        this.hours = hours;
    }

    //Getter functions.
    public Branch getBranch(){
        return branch;
    }

    public String getDay(){
        return day;
    }

    public String getHours(){
        return hours;
    }
}

