package com.example.a2105projectgroup13;

public class Document extends Requirement {
    private String type;
    private String name;
    private String fileType;
    private Service service;

    public Document(){
    }

    public Document(String type, String documentName, String fileType){
        this.type = type;
        this.name = documentName;
        this.fileType = fileType;
        this.service = new Service();
    }

    public String getType(){return type;}

    public String getName(){return name;}

    public String getFileType(){return fileType;}

    public String toString(){
        return getName() + ": " + "\n" + "      type: " + getType() +
                "\n" + "      fileType: " + getFileType();
    }

    public void setService(Service newService){
        service = newService;
    }

}
