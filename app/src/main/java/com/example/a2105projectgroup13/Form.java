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
    private String name;;

    public Form(){
    }

    public Form(String type, String formName){
        this.type = type;
        this.name = formName;
    }

    public String getType (){
        return type;
    }

    public String getName(){
        return name;
    }

}
