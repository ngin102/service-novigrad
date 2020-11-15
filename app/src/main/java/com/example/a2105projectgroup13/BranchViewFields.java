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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class allows a Branch to view and the fields of a form of a service.
 */

public class BranchViewFields extends AppCompatActivity {
    // instance variables
    private DatabaseReference requirementInDatabase;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference fieldsInDatabase;

    private Intent requirementScreen;
    private String serviceName;
    private String requirementName;

    private ListView fieldList;
    private TextView requirementNameOnScreen;
    private ArrayList<String> fieldArrayList = new ArrayList<String>();

    private ArrayList<String> fieldKeyArrayList = new ArrayList<String>();

    private Button backToServiceListButton;
    private Button backToOfferedServicesListButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_fields);

        fieldList = (ListView) findViewById(R.id.branchFieldScrollingList);

        requirementScreen = getIntent();
        serviceName = requirementScreen.getStringExtra("selectedServiceName");
        requirementName = requirementScreen.getStringExtra("requirementName");

        firebaseDatabase = firebaseDatabase.getInstance();
        requirementInDatabase = firebaseDatabase.getReference("Services").child(serviceName).child(requirementName);
        fieldsInDatabase = requirementInDatabase.child("fields");

        requirementNameOnScreen = (TextView) findViewById(R.id.branchChangeableRequirementName);
        requirementNameOnScreen.setText(requirementName);

        backToServiceListButton = (Button) findViewById(R.id.branchBackToServicesButton2);
        backToServiceListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToServices = new Intent(BranchViewFields.this, BranchViewAdminServiceList.class);
                startActivity(moveToServices);
            }
        });


        backToOfferedServicesListButton = (Button) findViewById(R.id.branchOfferedServicesButton2);
        backToOfferedServicesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToRequirements = new Intent(BranchViewFields.this, BranchViewOfferedServiceList.class);
                moveToRequirements.putExtra("serviceName", serviceName);
                startActivity(moveToRequirements);
            }
        });


        fieldsInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fieldArrayList.clear();
                fieldKeyArrayList.clear();
                for (DataSnapshot field : snapshot.getChildren() ) {
                    String fieldValue = field.getValue(String.class);
                    fieldArrayList.add(fieldValue);
                    fieldKeyArrayList.add(field.getKey());
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(BranchViewFields.this, android.R.layout.simple_list_item_1, fieldArrayList);
                fieldList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchViewFields.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });
    }
}