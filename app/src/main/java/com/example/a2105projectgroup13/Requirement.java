package com.example.a2105projectgroup13;

public class Requirement {

    private String type;
    private String name;;

    private Service service;

    public Requirement(){
    }

    public Requirement(String type, String formName){
        this.type = type;
        this.name = formName;
        this.service = new Service();
    }

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
