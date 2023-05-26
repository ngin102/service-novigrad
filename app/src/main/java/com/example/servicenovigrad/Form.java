package com.example.servicenovigrad;

public class Form extends Requirement{

    private String type;
    private String name;;
    private Service service;

    //For firebase purposes
    public Form(){
    }

    public Form(String type, String formName){
        this.type = type;
        this.name = formName;
        this.service = new Service();
    }

    public String getType (){
        return type;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return getName() + "\n" + "      type: " + getType() +
                "\n" + "      TAP TO VIEW FIELDS";
    }

    public void setService(Service newService){
        service = newService;
    }

    public Service getService(){
        return service;
    }


}
