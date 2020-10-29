package com.example.a2105projectgroup13;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Service {
    private String name;
    //private DatabaseReference reference;
    private FirebaseDatabase firebaseDatabase;

    public Service(String name) {
        this.name = name;
        //this.reference = firebaseDatabase.getReference("Services").child(name).setValue();
    }

    public String getName(){
        return name;
    }
    /**
    public DatabaseReference getDatabaseReference(){
        return reference;
    }
     */
}
