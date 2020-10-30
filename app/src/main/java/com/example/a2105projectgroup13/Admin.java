package com.example.a2105projectgroup13;

/**
 * This class is the Admin class.
 * It can be used to create instances of type Admin, a subclass of User.
 */
public class Admin extends User{
    public Admin(String firstName, String lastName, String accountType) {
        super(firstName, lastName, accountType);
    }

    /*
    Creates a new Service object. This is implemented as a method of Admin such that
    only the admin may create a new service.
    */
    public Service createService(String serviceName, String price){
        return new Service(serviceName, price);
    }

    /*
    Creates a new Form object. This is implemented as a method of Admin such that
    only the admin may create a new form.
    */
    public Form createForm(String type, String formName){
        return new Form(type, formName);
    }

    /*
    Creates a new Document object. This is implemented as a method of Admin such that
    only the admin may create a new document.
    */
    public Document createDocument(String type, String documentName, String fileType, String description){
        return new Document(type, documentName, fileType, description);
    }


}