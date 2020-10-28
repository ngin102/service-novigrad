package com.example.a2105projectgroup13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewFields extends AppCompatActivity {
    private DatabaseReference requirementInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference fieldsInDatabase;

    private Intent requirementScreen;
    private String serviceName;
    private String requirementName;

    private ListView fieldList;
    private ArrayList<String> fieldArrayList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service_requirements);

        fieldList = (ListView) findViewById(R.id.serviceRequirementList);

        requirementScreen = getIntent();
        serviceName = requirementScreen.getStringExtra("selectedServiceName");
        requirementName = requirementScreen.getStringExtra("requirementName");

        firebaseDatabase = firebaseDatabase.getInstance();
        requirementInDatabase = firebaseDatabase.getReference("Services").child(serviceName).child(requirementName);
        fieldsInDatabase = requirementInDatabase.child("fields");

        fieldsInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fieldArrayList.clear();
                for (DataSnapshot field : snapshot.getChildren() ) {
                    String fieldValue = field.getValue(String.class);
                    fieldArrayList.add(fieldValue);
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(ViewFields.this, android.R.layout.simple_list_item_1, fieldArrayList);
                fieldList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFields.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });

    }
}