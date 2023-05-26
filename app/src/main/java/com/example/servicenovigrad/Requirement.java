package com.example.servicenovigrad;

public class Requirement {

    //Instance variables
    private String type;
    private String name;;

    private Service service;

    //Constructors
    public Requirement(){
    }

    public Requirement(String type, String formName){
        this.type = type;
        this.name = formName;
        this.service = new Service();
    }

    // Getter functions
    public String getType (){
        return type;
    }

    public String getName(){
        return name;
    }

    public Service getService(){
        return service;
    }

    public void setService(Service newService){
        service = newService;
    }

}
