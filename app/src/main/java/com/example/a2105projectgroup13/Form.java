package com.example.a2105projectgroup13;

public class Form {

    private int checkBoxes;
    private String type;
    private String[] fields;
    private String formName;

    public Form(int checkBoxes, String type, String formName){
        this.checkBoxes = checkBoxes;
        this.type = type;
        String[] fields = new String[checkBoxes];
        this.fields = fields;
        this.formName = formName;
    }

    public int getCheckBoxes(){return checkBoxes;}

    public String getTypeForm(){return type;}

    public String[] getFieldName(){return fields;}

    public String getFormName(){return formName;}
}
