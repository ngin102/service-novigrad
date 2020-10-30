package com.example.a2105projectgroup13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Form extends Requirement{

    private String type;
    private String name;;
    private Service service;
    private List<String> fields;

    public Form(){
    }

    public Form(String type, String formName){
        this.type = type;
        this.name = formName;
        this.service = new Service();
        this.fields = new ArrayList<String>();
    }

    public String getType (){
        return type;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        Object[] stringFields = fields.toArray();

        return getName() + ": " + "\n" + "      type: " + getType() +
                "\n" + "      fields: " + Arrays.toString(stringFields);
    }

    public void setService(Service newService){
        service = newService;
    }

    public List<String> getFields() {
        return fields;
    }

    public void addToFields(String i){
        fields.add(i);
    }


}
