package com.example.a2105projectgroup13;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.INVISIBLE;

public class User {

    private String firstName;
    private String lastName;
    private String accountType;
    // Variables used to access Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    // constructors
    public User(String firstName, String lastName, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
    }

    public User(String userID) {
        //TODO: fix this constructor..
        // initialize FirebaseAuthorization and FirebaseDatabase
        firebaseDatabase.getInstance();
        this.firstName = firebaseDatabase.getReference("Users").child(userID).child("firstName").toString();
        this.lastName = firebaseDatabase.getReference("Users").child(userID).child("lastName").toString();
        this.accountType = firebaseDatabase.getReference("Users").child(userID).child("accountType").toString();
    }

// public getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAccountType() {
        return accountType;
    }
}