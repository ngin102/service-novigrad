package com.example.a2105projectgroup13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is the Admin class.
 * It can be used to create instances of type Admin, a subclass of User.
 *
 * Please note: Admin, Customer and Branch all have unique operations that set them apart from one another
 * and from their parent class, User. However, these operations are not implemented in this Deliverable,
 * but they will be implemented in future Deliverables.
 */
public class Admin extends User{

    private List<Service> services;

    public Admin(String firstName, String lastName, String accountType) {
        super(firstName, lastName, accountType);
        services = new ArrayList<Service>();
    }

    public Service createService(String serviceName, String price){
        return new Service(serviceName, price);
    }
    public Form createForm(String type, String formName){
        return new Form(type, formName);
    }
    public Document createDocument(String type, String documentName, String fileType, String description){
        return new Document(type, documentName, fileType, description);
    }

    public List<Service> getServices() {
        return services;
    }

    public void addToServices(Service i){
        services.add(i);
    }

}