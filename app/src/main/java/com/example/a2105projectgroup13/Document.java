package com.example.a2105projectgroup13;

public class Document {
    private String type;
    private String documentName;
    private String fileType;

    public Document(String type, String documentName, String fileType){
        this.type = type;
        this.documentName = documentName;
        this.fileType = fileType;
    }

    public String getType(){return type;}

    public String getDocumentName(){return documentName;}

    public String getFileType(){return fileType;}
}
