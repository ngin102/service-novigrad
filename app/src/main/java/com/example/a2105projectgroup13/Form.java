package com.example.a2105projectgroup13;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Form {

    private String type;
    private String formName;;
    private String[] formFields;
    private String serviceName;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference fieldsInDatabase;
    private ArrayList<String> fieldArrayList = new ArrayList<String>();

    public Form(){
    }

    public Form(String type, String formName, String[] formFields){
        this.type = type;
        this.formName = formName;
        this.formFields = formFields;
    }

    public String getType (){
        return type;
    }

    public String getFormName(){
        return formName;
    }

    public String[] getFormFields() {
        return formFields;
    }

}
