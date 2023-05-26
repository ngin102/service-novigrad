package com.example.servicenovigrad;

public class Document extends Requirement {
    private String type;
    private String name;
    private String fileType;
    private String description;
    private Service service;

    public Document(){
    }

    public Document(String type, String documentName, String fileType, String description){
        this.type = type;
        this.name = documentName;
        this.fileType = fileType;
        this.description = description;
        this.service = new Service();
    }

    public String getType(){return type;}

    public String getName(){return name;}

    public String getDescription(){return description;}

    public String getFileType(){return fileType;}

    public String toString(){
        return getName() + "\n" + "      type: " + getType() +
                "\n" + "      fileType: " + getFileType() +   "\n" + "      description: " + (getDescription());
    }

    public void setService(Service newService){
        service = newService;
    }

    public Service getService(){
        return service;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

}
