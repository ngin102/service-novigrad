package com.example.a2105projectgroup13;

public class Document {
    private String type;
    private String documentName;

    public Document(String type, String doucementName){
        this.type = type;
        this.documentName = documentName;
    }

    public String getTypeDocument(){return type;}

    public String getDocumentName(){return documentName;}
}
