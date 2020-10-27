package com.example.a2105projectgroup13;

import java.util.ArrayList;

public class Form {

    private String type;
    private String formName;;

    public Form(String type, String formName){
        this.type = type;
        this.formName = formName;
    }

    public String getType (){
        return type;
    }

    public String getFormName(){
        return formName;
    }
}
