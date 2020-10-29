package com.example.a2105projectgroup13;

public class Document {
    private String type;
    private String name;
    private String fileType;

    public Document(){
    }

    public Document(String type, String documentName, String fileType){
        this.type = type;
        this.name = documentName;
        this.fileType = fileType;
    }

    public String getType(){return type;}

    public String getName(){return name;}

    public String getFileType(){return fileType;}

    //public int getNumberOfFields()

}
