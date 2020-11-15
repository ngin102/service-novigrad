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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class displays the existing requirements of a service (forms and documents) to the admin.
 */
public class BranchViewServiceRequirements extends AppCompatActivity {
    // instance variables
    private DatabaseReference serviceInDatabase;
    private FirebaseDatabase firebaseDatabase;

    private ListView requirementList;
    private ArrayList<String> requirementWithDetailsArrayList = new ArrayList<String>();
    private ArrayList<String> requirementWithoutDetailsArrayList = new ArrayList<String>();

    private Intent previousScreen;
    private String serviceName;

    private Form form;
    private Document document;

    private TextView requirementsListServiceName;

    private Button backToOfferedServiceListButton;
    private Button backToServiceListButton;

    // instance methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_view_service_requirements);

        requirementList = (ListView) findViewById(R.id.branchServiceRequirementList);

        previousScreen = getIntent();
        serviceName = previousScreen.getStringExtra("serviceName");

        firebaseDatabase = firebaseDatabase.getInstance();
        serviceInDatabase = firebaseDatabase.getReference("Services").child(serviceName);

        form = new Form();
        document = new Document();

        requirementsListServiceName = (TextView) findViewById(R.id.branchRequirementsListServiceName);
        requirementsListServiceName.setText(serviceName);

        backToServiceListButton = (Button) findViewById(R.id.branchBackToServicesButton);
        backToOfferedServiceListButton = (Button) findViewById(R.id.branchOfferedServicesButton1);

        // launch ServiceList created by Admin (no edit abilities)
        backToServiceListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToServices = new Intent(BranchViewServiceRequirements.this, BranchViewAdminServiceList.class);
                startActivity(moveToServices);
            }
        });


        // launch Offered Services List
        backToOfferedServiceListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent moveToServices = new Intent(BranchViewServiceRequirements.this, BranchViewOfferedServiceList.class);
                startActivity(moveToServices);
            }
        });

        // update displayed data when any data is changed
        serviceInDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requirementWithDetailsArrayList.clear();
                requirementWithoutDetailsArrayList.clear();

                String cost = "";
                for (DataSnapshot requirement: snapshot.getChildren()){
                    if (requirement.getKey().equals("price")) {
                        cost = requirement.getValue(String.class);
                        requirementWithDetailsArrayList.add("Price: $" + cost);
                        requirementWithoutDetailsArrayList.add(requirement.getKey());
                    }
                }

                Service selectedService = new Service(serviceName, cost);

                for (DataSnapshot requirement : snapshot.getChildren()) {
                    if (requirement.hasChild("fields")) {
                        form = requirement.getValue(Form.class);

                        requirementWithDetailsArrayList.add("Form: " + form.toString());
                        requirementWithoutDetailsArrayList.add(requirement.getKey());

                    } else if (requirement.getKey().equals("price")) {
                        ;
                    } else {
                        document = requirement.getValue(Document.class);
                        requirementWithDetailsArrayList.add("Document: " + document.toString());
                        requirementWithoutDetailsArrayList.add(requirement.getKey());
                    }
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(BranchViewServiceRequirements.this, android.R.layout.simple_list_item_1, requirementWithDetailsArrayList);
                requirementList.setAdapter(arrayAdapter);

            }

            // display error if there is a problem displaying the data from the database
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BranchViewServiceRequirements.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


        //Only for fields
        requirementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String requirementToView = requirementWithoutDetailsArrayList.get(i);

                String checkIfFormOrDocument = requirementWithDetailsArrayList.get(i);

                if (checkIfFormOrDocument.indexOf(":") == 4){
                    moveToFields(requirementToView);
                }
            }
        });
    }

    /**
     * Helper method for launching ViewFields to edit form requirements.
     * Returns nothing.
     */
    private void moveToFields(final String requirementToViewFields){
        Intent moveToView = new Intent(BranchViewServiceRequirements.this, BranchViewFields.class);
        moveToView.putExtra("selectedServiceName", serviceName);
        moveToView.putExtra("requirementName", requirementToViewFields);
        startActivity(moveToView);
    }
}