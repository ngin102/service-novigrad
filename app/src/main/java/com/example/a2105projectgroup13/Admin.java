package com.example.a2105projectgroup13;

/**
 * This class is the Admin class.
 * It can be used to create instances of type Admin, a subclass of User.
 *
 * Please note: Admin, Customer and Branch all have unique operations that set them apart from one another
 * and from their parent class, User. However, these operations are not implemented in this Deliverable,
 * but they will be implemented in future Deliverables.
 */
public class Admin extends User{
    public Admin(String firstName, String lastName, String accountType) {
        super(firstName, lastName, accountType);
    }

    public Service createService(String serviceName){
        return new Service(serviceName);
    }
    public Form createForm(String type, String formName){
        return new Form(type, formName);
    }
    public Document createDocument(String type, String documentName, String fileType){
        return new Document(type, documentName, fileType);
    }


}