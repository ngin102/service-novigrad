package com.example.a2105projectgroup13;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityServiceRequest extends AppCompatActivity {
    //instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private ArrayList<String> formAndFieldsArrayList = new ArrayList<String>();
    private ArrayList<String> documentsArrayList = new ArrayList<String>();

    private Intent previousScreen;
    private String requestKey;
    private String branchId;

    private ListView formFilledFieldsListView, documentsListView;

    private TextView changeableServiceRequestTextView;
    private TextView changeableCustomerIDTextView;
    private TextView changeableServiceNameTextView;
    private TextView changeableServicePriceTextView;

    private Button acceptButton, rejectButton, backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);

        initializeInstanceVariables();

        getCustomerInfo();

        //Forms/Fields List
        DatabaseReference formsAndFieldsForThisBranch = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Fields");
        formsAndFieldsForThisBranch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                formAndFieldsArrayList.clear();
                for (DataSnapshot form : snapshot.getChildren()) {
                    String formAndFieldName = form.getKey();
                    String customerInputForField = form.getValue(String.class);
                    formAndFieldsArrayList.add(formAndFieldName + ": " + customerInputForField);
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(ActivityServiceRequest.this, android.R.layout.simple_list_item_1, formAndFieldsArrayList);
                formFilledFieldsListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityServiceRequest.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });



        //Documents List
        DatabaseReference documentsForThisBranch = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Documents");
        documentsForThisBranch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                documentsArrayList.clear();
                for (DataSnapshot form : snapshot.getChildren()) {
                    String documentName = form.getKey();
                    documentsArrayList.add(documentName);
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(ActivityServiceRequest.this, android.R.layout.simple_list_item_1, documentsArrayList);
                documentsListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityServiceRequest.this, "ERROR.", Toast.LENGTH_LONG).show();
            }
        });


        //The back button returns the user to the list of service requests received by the branch account
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityServiceRequest.this, BranchViewServiceRequests.class));
            }
        });

        //The accept action sets the status of the service request to "Accepted"
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest();
            }
        });

        //The reject action sets the status of the service request to "Rejected"
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectRequest();
            }
        });
    }

    /**
     * Initializes all of the instance variables.
     */
    private void initializeInstanceVariables() {
        //initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        //getting the branch id
        FirebaseUser branchAccount = firebaseAuth.getInstance().getCurrentUser();
        branchId = branchAccount.getUid();

        //initialize buttons
        acceptButton = (Button)findViewById(R.id.acceptButton);
        rejectButton = (Button)findViewById(R.id.rejectButton);
        backButton = (Button)findViewById(R.id.backButton);

        //initialize ListViews
        formFilledFieldsListView = (ListView)findViewById(R.id.formFilledFieldsListView);
        documentsListView = (ListView)findViewById(R.id.attachmentsListView);

        //initialize TextViews
        changeableServiceRequestTextView = (TextView)findViewById(R.id.changeableServiceRequestTextView);
        changeableCustomerIDTextView = (TextView)findViewById(R.id.changeableCustomerIDTextView);
        changeableServiceNameTextView = (TextView)findViewById(R.id.changeableServiceNameTextView);;
        changeableServicePriceTextView = (TextView)findViewById(R.id.changeableServicePriceTextView);

        //get intent
        previousScreen = getIntent();
        requestKey = previousScreen.getStringExtra("requestKey");
    }

    private void getCustomerInfo(){

        DatabaseReference customerIDReference = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("CustomerID");
        DatabaseReference serviceNameReference = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Service");
        DatabaseReference priceReference = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Price");
        DatabaseReference statusReference = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Status");


        customerIDReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                changeableCustomerIDTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ActivityServiceRequest.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        serviceNameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                changeableServiceNameTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ActivityServiceRequest.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        priceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                changeableServicePriceTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ActivityServiceRequest.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });

        statusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                changeableServiceRequestTextView.setText(requestKey + " (Status: " + value + ")");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ActivityServiceRequest.this, "ERROR", Toast.LENGTH_SHORT).show();;
            }
        });
    }

    /**
     * Sets the service request status to "Accepted"
     */
    private void acceptRequest() {
        DatabaseReference statusReference = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Status");
        statusReference.setValue("Accepted");
    }

    /**
     * Sets the service request status to "Rejected"
     */
    private void rejectRequest() {
        DatabaseReference statusReference = firebaseDatabase.getReference("Service Requests").child(branchId).child(requestKey).child("Status");
        statusReference.setValue("Rejected");
    }
}
