package com.example.a2105projectgroup13;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private String name;
    private String price;
    private List<Requirement> requirements;

    private Admin admin;

    public Service(){
    }

    public Service(String name, String price) {
        this.name = name;
        this.price = price;
        this.requirements = new ArrayList<Requirement>();
        //Every service is associated to the same Admin.
        this.admin = new Admin("Admin", "Admin,", "Admin Account");
    }

    public String getName(){
        return name;
    }

    public String getPrice() {
        return price;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void addToRequirements(Requirement i){
        requirements.add(i);
    }
}
